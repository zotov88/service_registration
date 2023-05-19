//Взято с https://bootstrap-4.ru/docs/5.0/forms/validation/
function validateFormSchedule() {
    'use strict'
    // Получите все формы, к которым мы хотим применить пользовательские стили проверки Bootstrap
    const forms = document.querySelectorAll('.needs-validation');
    const doctor = document.getElementById("doctor");
    const day = document.getElementById("day");
    const cabinet = document.getElementById("cabinet");
    // Зацикливайтесь на них и предотвращайте отправку
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }
                if (doctor.value === 'default') {
                    alert("Пожалуйста, выберите врача");
                    event.preventDefault()
                    event.stopPropagation()
                    return false;
                }
                if (day.value === 'default') {
                    alert("Пожалуйста, укажите день");
                    event.preventDefault()
                    event.stopPropagation()
                    return false;
                }
                if (cabinet.value === 'default') {
                    alert("Пожалуйста, укажите кабинет");
                    event.preventDefault()
                    event.stopPropagation()
                    return false;
                }
                form.classList.add('was-validated')
            }, false)
        })
}