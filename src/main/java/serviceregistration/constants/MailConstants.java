package serviceregistration.constants;

public interface MailConstants {

    String MAIL_MESSAGE_FOR_REMEMBER_PASSWORD = """
            Добрый день.\n
            Для восстановления пароля перейдите по ссылке: http://localhost:8080/clients/change-password?uuid=
            """;

    String MAIL_SUBJECT_FOR_REMEMBER_PASSWORD = "Восстановление пароля";

    String MAIL_SUBJECT_FOR_REGISTRATION = "Успешная регистрация к врачу";

    String MAIL_MESSAGE_AFTER_CREATED_MEET = "Вы успешно зарегистрированы к врачу";

}
