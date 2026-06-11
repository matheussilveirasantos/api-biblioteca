package br.com.escola.biblioteca.service;

import br.com.escola.biblioteca.entity.Livro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${biblioteca.email.destinatario}")
    private String destinatario;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void enviarEmailCadastroLivro(Livro livro) {
        String assunto = "📚 Novo Livro Cadastrado: " + livro.getTitulo();
        String corpo = buildEmailHtml(
            "Novo Livro Cadastrado",
            "Um novo livro foi adicionado ao catálogo da biblioteca.",
            livro,
            "#27ae60",
            "✅ CADASTRO REALIZADO"
        );
        enviar(assunto, corpo);
    }

    @Async
    public void enviarEmailAlteracaoLivro(Livro livro) {
        String assunto = "✏️ Livro Atualizado: " + livro.getTitulo();
        String corpo = buildEmailHtml(
            "Livro Atualizado",
            "Os dados de um livro foram alterados no catálogo.",
            livro,
            "#2980b9",
            "🔄 ATUALIZAÇÃO REALIZADA"
        );
        enviar(assunto, corpo);
    }

    @Async
    public void enviarEmailExclusaoLivro(Livro livro) {
        String assunto = "🗑️ Livro Excluído: " + livro.getTitulo();
        String corpo = buildEmailHtml(
            "Livro Excluído",
            "Um livro foi removido do catálogo da biblioteca.",
            livro,
            "#c0392b",
            "❌ EXCLUSÃO REALIZADA"
        );
        enviar(assunto, corpo);
    }

    private void enviar(String assunto, String corpo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(destinatario.split(","));
            helper.setSubject(assunto);
            helper.setText(corpo, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("[EmailService] Falha ao enviar e-mail: " + e.getMessage());
        }
    }

    private String buildEmailHtml(String titulo, String subtitulo, Livro livro,
                                   String corDestaque, String badge) {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8"/>
              <style>
                body { font-family: 'Segoe UI', Arial, sans-serif; background:#f4f6f8; margin:0; padding:20px; }
                .container { max-width:600px; margin:0 auto; background:#fff;
                             border-radius:12px; overflow:hidden;
                             box-shadow:0 4px 20px rgba(0,0,0,.12); }
                .header { background:%s; color:#fff; padding:32px 28px; text-align:center; }
                .header h1 { margin:0; font-size:26px; letter-spacing:.5px; }
                .badge { display:inline-block; background:rgba(255,255,255,.2);
                         color:#fff; font-size:11px; font-weight:700;
                         padding:4px 12px; border-radius:20px; margin-top:10px; }
                .body { padding:28px; }
                .subtitle { color:#666; font-size:15px; margin-bottom:24px; }
                .info-box { background:#f8f9fa; border-left:4px solid %s;
                            border-radius:6px; padding:18px; margin-bottom:20px; }
                .info-row { display:flex; margin-bottom:10px; font-size:14px; }
                .info-label { font-weight:700; color:#444; min-width:120px; }
                .info-value { color:#555; }
                .footer { background:#f4f6f8; text-align:center; padding:16px;
                          font-size:12px; color:#aaa; }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="header">
                  <h1>📚 Biblioteca Digital</h1>
                  <div class="badge">%s</div>
                  <p style="margin:8px 0 0;opacity:.85;font-size:18px;font-weight:600">%s</p>
                </div>
                <div class="body">
                  <p class="subtitle">%s</p>
                  <div class="info-box">
                    <div class="info-row"><span class="info-label">📖 Título:</span>
                      <span class="info-value">%s</span></div>
                    <div class="info-row"><span class="info-label">🔢 ISBN:</span>
                      <span class="info-value">%s</span></div>
                    <div class="info-row"><span class="info-label">📅 Ano:</span>
                      <span class="info-value">%d</span></div>
                    <div class="info-row"><span class="info-label">✍️ Autor:</span>
                      <span class="info-value">%s</span></div>
                    <div class="info-row"><span class="info-label">🏷️ Gênero:</span>
                      <span class="info-value">%s (%s)</span></div>
                    <div class="info-row"><span class="info-label">🏢 Editora:</span>
                      <span class="info-value">%s</span></div>
                  </div>
                </div>
                <div class="footer">
                  Sistema Biblioteca Digital &bull; Notificação automática
                </div>
              </div>
            </body>
            </html>
            """.formatted(
                corDestaque, corDestaque, badge, titulo, subtitulo,
                livro.getTitulo(), livro.getIsbn(), livro.getAnoPublicacao(),
                livro.getAutor().getNome(),
                livro.getGenero().getNome(), livro.getGenero().getSigla(),
                livro.getEditora().getNome()
            );
    }
}
