function menuToggle(x) {
    var dropdown = document.getElementById('openMenu');
    var originalIcon = document.getElementById('navIcon');

    if (dropdown.style.display === 'block') {
        dropdown.style.display = 'none';
        originalIcon.style.display = 'block';
    } else {
        dropdown.style.display = 'block';
        originalIcon.style.display = 'none';        
    }
}
