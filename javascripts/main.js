function locale_ES() {
    document.getElementById('ENflag').src = 'https://filedn.com/l5rBk3Fq5UWhiuXSCD0KC04/ATTACHMENTS/Images/Flags/great-britain_grey.png';
    document.getElementById('ESflag').src = 'https://filedn.com/l5rBk3Fq5UWhiuXSCD0KC04/ATTACHMENTS/Images/Flags/spain_color.png';
    for (e of document.querySelectorAll('.ES')) {
        e.style.display = '';
    };
    for (e of document.querySelectorAll('.EN')) {
        e.style.display = 'none';
    };
};

function locale_EN() {
    document.getElementById('ESflag').src = 'https://filedn.com/l5rBk3Fq5UWhiuXSCD0KC04/ATTACHMENTS/Images/Flags/spain_grey.png';
    document.getElementById('ENflag').src = 'https://filedn.com/l5rBk3Fq5UWhiuXSCD0KC04/ATTACHMENTS/Images/Flags/great-britain_color.png';
    for (e of document.querySelectorAll('.EN')) {
        e.style.display = '';
        e.style.color = '';
        e.style.fontFamily = '';
        e.style.fontStyle = '';
    };
    for (e of document.querySelectorAll('.ES')) {
        e.style.display = 'none';
    };
    for (e of document.querySelectorAll('.aux')) {
        e.style.display = 'none';
    }
};