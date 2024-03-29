package ta.simonita.monita.controller;


import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ta.simonita.monita.model.FaskesModel;
import ta.simonita.monita.model.UserModel;
import ta.simonita.monita.repository.FaskesDb;
import ta.simonita.monita.service.FaskesService;
import ta.simonita.monita.service.ServerProperties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    FaskesService faskesService;

    @Autowired
    ServerProperties serverProperties;



    @RequestMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("faskes", new FaskesModel());
        model.addAttribute("listKelurahan",serverProperties.getKelurahan());
        return "page-register";
    }

    @PostMapping("/register")
    public String registerNewUser(
            @ModelAttribute FaskesModel faskes,
            Model model,
            RedirectAttributes redirect
    ){
        faskesService.addFaskes(faskes);

        model.addAttribute("msg","Akun anda sudah dibuat, silahkan login");
        return "redirect:/login";

    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("masuk_first",true);
        return "page-forget-password";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        System.out.println("ini email "+email);
        String token = RandomString.make(30);

        try {
            if (faskesService.updateResetPasswordToken(token, email) == null){
                throw new UsernameNotFoundException("Email tidak ditemukan");
            }
            String resetPasswordLink = getSiteURL(request) + "/user/reset_password?token=" + token;
            sendmail(email,resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "page-forget-password";
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model, RedirectAttributes redir) {
        FaskesModel user = faskesService.getFaskesByResetToken(token);
        model.addAttribute("token", token);

        if (user == null) {
            redir.addFlashAttribute("message", "Invalid Token");
            return "redirect:/login";
        }

        return "page-reset-password";

    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model, RedirectAttributes redir) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        System.out.println(token);

        FaskesModel user = faskesService.getFaskesByResetToken(token);
        if (user == null){
            user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }

        model.addAttribute("title", "Reset your password");

        if (user == null) {
            redir.addFlashAttribute("message", "Invalid Token");
            return "redirect:/login";
        } else {
            faskesService.updatePassword(user, password);
            redir.addFlashAttribute("message", "You have successfully changed your password.");
            return "redirect:/login";
        }

    }

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public void sendmail(String email, String resetPasswordLink) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sistem.monita@gmail.com", "Simonita123$");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("sistem.monita@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject("Reset Password SIMONITA");
        msg.setContent("Reset Password SIMONITA", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Klik link ini untuk mengganti password: \n\n"+ resetPasswordLink, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        System.out.println("ini body part "+ messageBodyPart.getContent().toString());

        msg.setContent(multipart);
        Transport.send(msg);
    }

    @GetMapping ("/profil")
    public String profilPage (Model model){
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("faskes", user);
        return "page-profile";
    }

    @GetMapping("/profil/ubah")
    public String ubahProfil (Model model){
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("faskes", user);
        return"page-ubahprofile";
    }

    @PostMapping("/profil/ubah/{id_faskes}")
    public String postUbahProfile (FaskesModel faskes, Model model,@PathVariable Long id_faskes, RedirectAttributes redirectAttributes){
        FaskesModel user = faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        faskes.setAddress(user.getAddress());
        faskesService.changeFaskes(faskes);
        return "redirect:/user/profil";
    }

    @GetMapping("/profil/ubah-password")
    public String ubahPassword (Model model){
        model.addAttribute("faskes", faskesService.getFaskesByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        return"page-ubahpassword";
    }

    @PostMapping("/profil/ubah-password")
    public String postUbahPassword (@ModelAttribute FaskesModel faskes, @RequestParam("confirmationpass") String confirmationpass,
                                    @RequestParam("newpass") String newpass, Model model,
                                    RedirectAttributes redirectAttributes,HttpServletRequest request){

        //cekpassword baru dan konfirmasinya
        if(newpass.equals(confirmationpass)){
            faskesService.updatePassword(faskes,newpass);
            redirectAttributes.addFlashAttribute("successUpdate", "Password " + faskes.getName() + " berhasil diperbaharui");
            return "redirect:/user/profil/ubah-password";
        }
        redirectAttributes.addFlashAttribute("errorUpdate", "Password " + faskes.getName() + " gagal diperbaharui");
        return "redirect:/user/profil/ubah-password";
    }
}
