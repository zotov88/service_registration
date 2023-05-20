//Взято с https://bootstrap-4.ru/docs/5.0/forms/validation/
function validateFormDoctorAdd() {
    'use strict'
    const forms = document.querySelectorAll('.needs-validation');
    const specialization = document.getElementById("specialization");
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }
                if (specialization.value === 'default') {
                    alert("Пожалуйста, выберите специализацию");
                    event.preventDefault()
                    event.stopPropagation()
                    return false;
                }
                form.classList.add('was-validated')
            }, false)
        })
}

function validateFormDoctorDelete() {
    'use strict'
    const forms = document.querySelectorAll('.needs-validation');
    const doctor = document.getElementById("doctor");
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
                form.classList.add('was-validated')
            }, false)
        })
}

function validateFormSchedule() {
    'use strict'
    const forms = document.querySelectorAll('.needs-validation');
    const doctor = document.getElementById("doctor");
    const day = document.getElementById("day");
    const cabinet = document.getElementById("cabinet");
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

function validateForm() {
    'use strict'
    const forms = document.querySelectorAll('.needs-validation');
    const gender = document.getElementById("gender");
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }
                if (gender.value === 'default') {
                    alert("Пожалуйста, укажите пол!");
                    event.preventDefault()
                    event.stopPropagation()
                    return false;
                }
                form.classList.add('was-validated')
            }, false)
        })
}