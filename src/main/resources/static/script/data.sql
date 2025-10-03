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
<!doctype html>
<html lang="en-US">

<head>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
    <title>Reset Password Email Template</title>
    <meta name="description" content="Reset Password Email Template.">
    <style type="text/css">
        a:hover {
            text-decoration: underline !important;
        }
    </style>
</head>

<body marginheight="0" topmargin="0" marginwidth="0" style="margin: 0px; background-color: #f2f3f8;" leftmargin="0">
    <table cellspacing="0" border="0" cellpadding="0" width="600" bgcolor="#f2f3f8"
        style="@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: ''Open Sans'', sans-serif;">
        <tr>
            <td>
                <table style="background-color: #f2f3f8; max-width:670px;  margin:0 auto;" width="600" border="0"
                    align="center" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="height:80px;">&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="text-align:center;">
                            <a href="%s" title="logo" target="_blank">
                                <img width="60"
                                    src="https://firebasestorage.googleapis.com/v0/b/toeicute-70460.appspot.com/o/logo_tachnen.png?alt=media&token=b1e89f15-9e51-4010-96bc-004946b3c586"
                                    title="logo" alt="logo">
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td style="height:20px;">&nbsp;</td>
                    </tr>
                    <tr>
                        <td>
                            <table width="500" border="0" align="center" cellpadding="0" cellspacing="0"
                                style="max-width:670px;background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);">
                                <tr>
                                    <td style="height:40px;">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td style="padding:0 35px;">
                                        <h1
                                            style="color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:''Rubik'',sans-serif;">
                                            Xin chào, bạn vừa gửi yêu cầu cấp lại mật khẩu.</h1>
                                        <span
                                            style="display:inline-block; vertical-align:middle; margin:29px 0 26px; border-bottom:1px solid #cecece; width:100px;"></span>
                                        <p
                                            style="color:#455056; font-size:16px; line-height:24px; margin:0; text-decoration: solid;">
                                            Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này. <br>
                                            Để cấp lại mật khẩu, vui lòng nhấn vào nút bên dưới.
                                        </p>
                                        <a href="%s" title="Reset Password" target="_blank"
                                            style="background:#20e277;text-decoration:none !important; font-weight:500; margin-top:35px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 24px;display:inline-block;border-radius:50px;">Cấp
                                            lại mật khẩu</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="height:40px;">&nbsp;</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</body>

</html>
        ');
