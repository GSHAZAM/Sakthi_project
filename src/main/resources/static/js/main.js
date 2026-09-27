// Client-side interactions for Urban Drone Delivery
document.addEventListener('DOMContentLoaded', function () {

    // Sidebar Toggle for Mobile
    const sidebarToggler = document.getElementById('sidebar-toggler');
    const sidebar = document.querySelector('.sidebar');

    if (sidebarToggler && sidebar) {
        sidebarToggler.addEventListener('click', function () {
            sidebar.classList.toggle('show');
        });
    }

    // Auto-dismiss Alert banners after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Tooltips Initialization
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
});
