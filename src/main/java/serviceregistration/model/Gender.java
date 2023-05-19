package serviceregistration.model;

public enum Gender {

    MALE("Мужчина"),
    FEMALE("Женщина");

    private final String genderText;

    Gender(String text) {
        this.genderText = text;
    }

    public String getGenderTextDisplay() {
        return this.genderText;
    }

}