INSERT INTO role (role_id, role_name)
VALUES (1, 'ADMIN'),
       (2, 'USER');
INSERT INTO user_account (avatar, created_at, email, full_name, password, status, updated_at)
VALUES (NULL, '2023-12-12 08:08:54.000000', 'admin', 'ADMIN',
        '$2a$10$nfyjdSZAnJQbNLY5DS3tqufpFWmNKimtrEvxMxn1QOSl/JpXyGYs.', 'ACTIVE', '2023-12-13 09:45:25.776202');
INSERT INTO user_role (user_id, role_id)
VALUES (1, 1);

insert into crawl_config(agent_user, token, email)
VALUES ('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
        'csrftoken=taoxMpbf7FKoFHoZGyNqfdoKcwD0elEJgIKsrel28Pc7cEKWiO5XB9Tc8Lw4r0ge; sessionid=11wlkenrvfpsrris51vl9aqbixlvcvfq; cf_clearance=.UxZwpQCxxLgA3nOPuc8.XCIZ3pXEIYNPBVDEK1r_r8-1711956447-1.0.1.1-MTG7UGOio0vwN9itKBMXt3e8nMGDHKmXBgnQfft.DVHqFqok0rtyy.QQ9imW79wiElSwe8jX0eKt8vHd9HyijQ',
        'hideonbush8405@gmail.com');
insert into email_config(host, port, username, password, status)
values ('smtp.gmail.com', '587', 'hideonbush8405', 'okcu xvrk eblg accw', true);


insert into email_template(name, status, subject, template_code, template_content)
VALUES ('Mẫu email xác thực OTP khi đăng ký', 'active', 'Xác thực tài khoản TOEICUTE',
        'AUTHENTICATION_AFTER_REGISTER',
        '<!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <meta http-equiv="X-UA-Compatible" content="ie=edge" />
            <title>Toeicute</title>
            <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet" />
        </head>

        <body>
            <div style="font-family: Helvetica,Arial,sans-serif;min-width:700px;overflow:auto;line-height:2">
                <div style="margin:50px auto;width:600px;padding:20px 0">
                    <div style="border-bottom:1px solid #eee">
                        <a href="" style="font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600">TOEICUTE</a>
                    </div>
                    <p style="font-size:1.1em">Hi, %s</p>
                    <p>Chào mừng bạn đến với TOEICUTE.<br />Mã OTP kích hoạt tài khoản của bạn là:</p>
                    <h2
                        style="background: #00466a; margin: 0 auto;width: max-content;padding: 0 10px;color: #fff; border-radius: 4px;">
                        %s
                    </h2>
                    <p style="font-size:0.9em;">
                        <br />
                        Nếu bạn không yêu cầu mã này thì có thể người khác đang cố truy cập vào tài khoản của bạn.
                        <br /><b>Không chuyển tiếp hoặc đưa mã này cho bất kỳ ai.</b>
                        <br />
                        <br />
                        Thân mến,
                        <br />
                        Toeicute
                    </p>
                    <hr style="border:none;border-top:1px solid #eee" />
                    <div style="padding:8px 0; color:#aaa; font-size:0.8em; line-height:1; font-weight:300">
                        <p>Email này không thể nhận được thư trả lời.</p>
                    </div>
                </div>
            </div>
        </body>
        </html>');


insert into email_template(name, status, subject, template_code, template_content)
VALUES ('Mẫu email sau khi đăng ký tài khoản bằng FB/GOOGLE', 'active', 'THÔNG TIN TÀI KHOẢN TOEICUTE', 'LOGIN_SOCIAL',
        '<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>Toeicute</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet" />
</head>

<body>
    <div style="
        font-family: Helvetica, Arial, sans-serif;
        min-width: 700px;
        overflow: auto;
        line-height: 2;
      ">
        <div style="margin: 50px auto; width: 600px; padding: 20px 0">
            <div style="border-bottom: 1px solid #eee">
                <a href="" style="
              font-size: 1.4em;
              color: #00466a;
              text-decoration: none;
              font-weight: 600;
            ">TOEICUTE</a>
            </div>
            <p style="font-size: 1.1em">Hi, %s</p>
            <p>
                Chào mừng bạn đến với TOEICUTE.
            </p>
            <ul style="font-size: 0.8em;">
                <li>Bạn có thể sử dụng thông tin này để đăng nhập vào TOEICUTE hoặc đăng nhập bằng tài khoản %s
                </li>
                <li>Sau khi đăng nhập, bạn có thể thay đổi mật khẩu tùy thích.</li>
                <li>Chúng tôi khuyến khích bạn nên thay đổi mật khẩu để bảo mật tài khoản.</li>
            </ul>
            <p>Tài khoản để đăng nhập vào
                TOEICUTE của bạn là:</p>
            <table
                style="border-collapse: collapse; width: 600px; border: 1px solid #ddd; font-size: 14px; margin-bottom: 20px;">
                <tr style="border-bottom: 1px solid #ddd;">
                    <td style="padding: 10px;">
                        <b>Tên đăng nhập:</b>
                    </td>
                    <td style="padding: 10px;">
                        <span>%s</span>
                    </td>
                </tr>
                <tr style="border-bottom: 1px solid #ddd;">
                    <td style="padding: 10px;">
                        <b>Họ và tên:</b>
                    </td>
                    <td style="padding: 10px;">
                        <span>%s</span>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 10px;">
                        <b>Mật khẩu:</b>
                    </td>
                    <td style="padding: 10px;">
                        <span>%s</span>
                    </td>
                </tr>
            </table>
            <p>
                <b style="font-size: 1.2em">Link đăng nhập: </b>
                <a href="%s">Toeicute</a>
            </p>

            <p style="font-size: 0.9em">
                <br /><b>Không chuyển tiếp hoặc đưa email này cho bất kỳ ai.</b>
                <br />
                <br />
                Thân mến,
                <br />
                <span class="fst-italic">Toeicute</span>
            </p>
            <hr style="border: none; border-top: 1px solid #eee" />
            <div style="
            padding: 8px 0;
            color: #aaa;
            font-size: 0.8em;
            line-height: 1;
            font-weight: 300;
          ">
                <p>Email này không thể nhận được thư trả lời.</p>
            </div>
        </div>
    </div>
</body>

</html>
        ');



insert into email_template(name, status, subject, template_code, template_content)
VALUES ('Mẫu email cấp lại mật khẩu', 'active', 'Cấp lại mật khẩu TOEICUTE', 'FORGOT_PASSWORD',
        '
<table style="background-color: #f2f3f8; max-width: 540px; margin: 0 auto;">
    <tbody>
        <tr>
            <td style="height: 80px;">&nbsp;</td>
        </tr>
        <tr>
            <td style="text-align: center;"><a title="logo" href="%s" target="_blank" rel="noopener">
                    <img title="logo"
                        src="https://firebasestorage.googleapis.com/v0/b/toeicute-70460.appspot.com/o/logo_tachnen.png?alt=media&amp;token=b1e89f15-9e51-4010-96bc-004946b3c586"
                        alt="logo" width="60"> </a></td>
        </tr>
        <tr>
            <td style="height: 20px;">&nbsp;</td>
        </tr>
        <tr>
            <td>
                <table
                    style="max-width: 540px; background: #fff; border-radius: 3px; text-align: center; -webkit-box-shadow: 0 6px 18px 0 rgba(0,0,0,.06); -moz-box-shadow: 0 6px 18px 0 rgba(0,0,0,.06); box-shadow: 0 6px 18px 0 rgba(0,0,0,.06);">
                    <tbody>
                        <tr>
                            <td style="height: 40px;">&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="padding: 0 35px;">
                                <h1
                                    style="color: #1e1e2d; font-weight: 500; margin: 0; font-size: 32px; font-family: ''Rubik'',sans-serif;">
                                    Xin ch&agrave;o, bạn vừa gửi y&ecirc;u cầu cấp lại mật khẩu.</h1>
                                <p
                                    style="color: #455056; font-size: 16px; line-height: 24px; margin: 0; text-decoration: solid;">
                                    Nếu bạn kh&ocirc;ng thực hiện y&ecirc;u cầu n&agrave;y, vui
                                    l&ograve;ng bỏ qua email n&agrave;y. <br>Để cấp lại mật khẩu, vui
                                    l&ograve;ng nhấn v&agrave;o n&uacute;t b&ecirc;n dưới.</p>
                                <a style="background: #20e277; text-decoration: none !important; font-weight: 500; margin-top: 35px; color: #fff; text-transform: uppercase; font-size: 14px; padding: 10px 24px; display: inline-block; border-radius: 50px;"
                                    title="Reset Password" href="%s" target="_blank" rel="noopener">Cấp
                                    lại mật khẩu</a>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 40px;">&nbsp;</td>
                        </tr>
                    </tbody>
                </table>
            </td>
        </tr>
    </tbody>
</table>
        ');

insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Home', 'mdi mdi-home menu-icon', 1, false, '/admin', '', 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('User Management', 'mdi mdi-account-circle menu-icon', 2, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Topic Exam Management', 'mdi mdi-database menu-icon', 3, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Exam Management', 'mdi mdi-book-open menu-icon', 4, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Score Management', 'mdi mdi-checkbox-marked-circle menu-icon', 5, false, '/admin/score', null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Transcript', 'mdi mdi-tooltip-text menu-icon', 6, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Crawl', 'mdi mdi-database-plus menu-icon', 7, true, null, null, 'MEMBER');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Blog', 'mdi mdi mdi-clipboard-text menu-icon', 8, true, null, null, 'MEMBER');

insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('System Monitoring', 'mdi mdi mdi-android menu-icon', 9, false, 'http://localhost:8080/api/applications', null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Firebase Config', 'mdi mdi-cloud-upload menu-icon', 10, true, null, null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Email Config', 'mdi mdi-email menu-icon', 11, true, null, null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Slider Config', 'mdi mdi-equal-box menu-icon', 12, true, null, null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Rev AI Config', 'mdi mdi-at menu-icon', 13, true, null, null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Kommunicate Config', 'mdi mdi-earth menu-icon', 14, true, null, null, 'SYSTEM');
insert into menu_group(display_name, icon, priority, have_child, path, roles, type) VALUES ('Tiny Editor Config', 'mdi mdi-widgets menu-icon', 14, true, null, null, 'SYSTEM');


insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Member Management', '/admin/users-management', null, 2);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Member Activity', '/admin/users-activity', null, 2);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Topic', '/admin/topic/list', null, 3);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Topic', '/admin/topic/add', null, 3);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Exam', '/admin/exam/list', null, 4);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Exam', '/admin/exam/add', null, 4);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Get Transcript', '/admin/transcript/get', null, 6);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Transcript History', '/admin/transcript/history', null, 6);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Crawl Data', '/admin/crawl/get', null, 7);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Config Crawl', '/admin/crawl/config', null, 7);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Crawl History', '/admin/crawl/list', null, 7);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Blog', '/admin/blog/get', null, 8);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Blog', '/admin/blog/add', null, 8);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Firebase Config', '/admin/firebase/list', null, 10);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Config', '/admin/firebase/update', null, 10);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('History Upload', '/admin/firebase/history', null, 10);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Email Config', '/admin/email/account/list', null, 11);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Email Config', '/admin/email/account/update', null, 11);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Template Email', '/admin/email/template-email/list', null, 11);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Template Email', '/admin/email/template-email/update', null, 11);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Slider', '/admin/slider/list', null, 12);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Slider', '/admin/slider/update', null, 12);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List RevAi Account', '/admin/rev-ai/account/list', null, 13);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add RevAi Account', '/admin/rev-ai/account/update', null, 13);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List RevAi Config', '/admin/rev-ai/config/list', null, 13);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add RevAi Config', '/admin/rev-ai/config/update', null, 13);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Kommunicate Account', '/admin/kommunicate/account/list', null, 14);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Kommunicate Account', '/admin/kommunicate/account/update', null, 14);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Kommunicate Config', '/admin/kommunicate/bot/list', null, 14);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Kommunicate Config', '/admin/kommunicate/bot/update', null, 14);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Tiny Account', '/admin/tiny/account/list', null, 15);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Tiny Account', '/admin/tiny/account/update', null, 15);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('List Tiny Config', '/admin/tiny/config/list', null, 15);
insert into left_menu(display_name, path, roles, menu_group_id) VALUES ('Add Tiny Config', '/admin/tiny/config/update', null, 15);


insert into revai_config(access_token, status)
    value ('02Nr61oQry1wxj-hsCiaMUmAAdfM_VWYGTcFXiNxB0RwmozYdeLpBRde7O_wmZ63pE18gsxYor7OOm8tLbunHjuKma3e0', true);

INSERT INTO chatai (location, model_name, project_id, prompt, status, token, type, url) VALUES ('us-central1', 'gemini-1.0-pro', 'toeicute-70460', 'Rule for answering this question: As an English tutor specializing in TOEIC exam preparation and English language learning, the chatbot is dedicated to addressing inquiries pertinent to these subjects. Should the user inquire about unrelated topics, the chatbot will courteously guide them back to the main theme by posing relevant questions. This ensures clarity in discussion and adherence to the chatbot\'s designated purpose of assisting users with TOEIC and English language learning.
Start a conversation like "Hello!" and encourage them like "No worries!" when you feel they do not understand. Question: %s', true, '{
  "type": "service_account",
                                                                                                            "project_id": "toeicute-70460",
                                                                                                            "private_key_id": "8099381cf1dfc903ebc36dcbf42bc95a9391526b",
                                                                                                            "private_key": "-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDdiBE4j1WI8SY+
8vWPbWrzEI481q9Q/rP1qj0Dnm+uPWz6HBvCDHKOCttAuemF4JrnUngtpUOh7lZV
3e0VAuNlNSzzlpQ+AvtPcnk1vKCaPdAZ1NcS0L9mdqjTtMSM3UOn4CRTvEEr81m0
Gb178HfcQlLqHANUTl785HVRXR/AkD6OEZTGhW0iisPTbb3oM4CqaNQfIIHMmlTS
vA2RlMFPZew4Iut1fB5B/C7ipNHecaO1Rg4mMY9aOGSIDM35DqqW4Z0vC9qpqzpz
o1FpDZcwol8hAkKD5ov3QolPrAUhtZ/nZkmUow8T5TjJkmefiQP5ZDsPbEkeAhH5
zAQ9oC7DAgMBAAECggEAJXcLDokzGodKBBBb1idXlIbiv0hBew2wSqZBTljfo1SR
FxVk3mLzhrEwVlh6tWEAYYmxeGrALuUbZccz/r8vzOB62zFGndwsO0ntVNizQpKc
nJnBy9VQ4VfFApTKPtZ2yC/ghObMEM5Fh+9oe8r1wmLXGe+l4n6kktliO+Hnak3K
LPdAp3SwDjPZPAWNqY0zG2xHyi+keG5U9SAbDwOBR05Cg64vzgqd0MpIgWT6507e
G91XigA7JtrWq7N7GJzrleQU6Hh8lygPIY5oJm5En6ZEhSCg6aJ953prFH9fIQQk
iJKBK1/8Kb/uUYdgcSHkgYkxraUt2//PPAPziybhkQKBgQDzVZQMVbTD8RSNw+qR
1fF4bdnG4Cu8lzx6LCwi5VtDfqprByX4FRFAsVSy1CJgj4tCm2KY+0yW+uXR5G99
nddbRBc8p6PkqpAe+Ggsav8FMNjVpfFMAIFFKfxllEjFHYhXI+UDL/OF0clT9aOR
Ur1/sH1RBcZH+fZFym3Skk70cQKBgQDpD/e3Jn3GkGfkag8gZ5sjZXGb9Dbq7MDC
feLd4rW5weohgwOLmza0DLVEVIGsa1mCnC6bd3e0VkTCjcLIVWUnUrSxePj/5uhs
zfz2J5aCP6dw/QU0l83rHg+ssKAb2S+1U/pzK564au5yukdev2WD85ZPls+9yOBz
LunLF15gcwKBgAg0oJNx6HCbFphZPPXCAyyOd8OPF6p6Zx2vk6VxmoxCHzkQuqUN
ANHvqO2G3cnnzUhnuQFzp/eHamyTiXbw9awsNaRj+tDhN47zzX+B9CuWpBlASUzI
z5MngU1jnLiY8FoP6c8btNiUZHeiVuu1oJJHv1IWIcOXmEhuNRneuS7hAoGBAI7u
O/0Wngwog46RfLVi1MHgdMwv+X4ZtB2TFo79enVxdyUWNBZqGiJRqNk++kfDjiGL
pwmxOrheL80hI1O5C1ASaL5uckZ+VgxkR76uiJ9Yalfg1FBsyNxokzRVHdv2Vkve
NLptBeoTqiiyFRMGTjueJ5Cn7L93ci8xli/3JOoBAoGBALX9JGfPLXcXuNVBJp6f
yv4XEhvhrlLP/mNf/LgVeoXzf75JRCYWF2bgBmlTFM8eWqGqW1MOYi8lg4PfCotr
/UaifK/U75rFYF6rHIcvFQ/jC3NHuKxj5MSZ/T2/zz5Nub0C5R9QQvvtKPTaL/f/
nUHQaVN+NCCWocW/aB2syt84
-----END PRIVATE KEY-----
",
                                                                                                            "client_email": "firebase-adminsdk-ajpvp@toeicute-70460.iam.gserviceaccount.com",
                                                                                                            "client_id": "109193298623065780489",
                                                                                                            "auth_uri": "https://accounts.google.com/o/oauth2/auth",
                                                                                                            "token_uri": "https://oauth2.googleapis.com/token",
                                                                                                            "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
                                                                                                            "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ajpvp%40toeicute-70460.iam.gserviceaccount.com",
                                                                                                            "universe_domain": "googleapis.com"
}
', 'GEMINI', null);
INSERT INTO chatai (location, model_name, project_id, prompt, status, token, type, url) VALUES ('', 'gpt-3.5-turbo', '', 'Rule for answering this question: As an English tutor specializing in TOEIC exam preparation and English language learning, the chatbot is dedicated to addressing inquiries pertinent to these subjects. Should the user inquire about unrelated topics, the chatbot will courteously guide them back to the main theme by posing relevant questions. This ensures clarity in discussion and adherence to the chatbot\'s designated purpose of assisting users with TOEIC and English language learning.
Start a conversation like "Hello!" and encourage them like "No worries!" when you feel they do not understand. Question: %s', true, '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', 'GPT', 'https://api.openai.com/v1/chat/completions');

insert into chatai(type, token, status, prompt, url) values ('GEMINI-CURL', 'AIzaSyBRL8Lz9vaftejrIqHAaHOZhuobfJFHgis', true, 'Rule for answering this question: As an English tutor specializing in TOEIC exam preparation and English language learning, the chatbot is dedicated to addressing inquiries pertinent to these subjects. Should the user inquire about unrelated topics, the chatbot will courteously guide them back to the main theme by posing relevant questions. This ensures clarity in discussion and adherence to the chatbot\'s designated purpose of assisting users with TOEIC and English language learning.
Start a conversation like "Hello!" and encourage them like "No worries!" when you feel they do not understand. Question: %s', 'https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=%s');
alter table calculate_score add index (total_question);
