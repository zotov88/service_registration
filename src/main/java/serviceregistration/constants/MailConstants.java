package serviceregistration.constants;

public interface MailConstants {

    String MAIL_MESSAGE_FOR_REMEMBER_PASSWORD = """
            Добрый день.
            Для восстановления пароля перейдите по ссылке:
            http://localhost:8080/clients/change-password?uuid=
            """;

    String MAIL_SUBJECT_FOR_REMEMBER_PASSWORD = "Восстановление пароля";

    String MAIL_SUBJECT_FOR_REGISTRATION_SUCCESS = "Успешная регистрация к врачу";

    String MAIL_SUBJECT_FOR_REGISTRATION_CANCEL =  "Регистрация к врачу отменена";

    String MAIL_SUBJECT_FOR_NOTIFY =  "Напоминание о записи к врачу";

    String MAIL_BODY_FOR_REGISTRATION_CANCEL =  "Ваша запись отменена";

    String MAIL_BODY_FOR_REGISTRATION_SUCCESS =  "Вы успешно зарегистрировались к врачу";

    String MAIL_BODY_FOR_NOTIFY =  "Напоминаем, что завтра у Вас запись к врачу";



}
