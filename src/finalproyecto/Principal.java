package finalproyecto;

/**
 * Punto de entrada de la aplicación.
 * Inicializa y conecta los componentes de la arquitectura MVC.
 */
public class Principal {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        new Controlador(vista, modelo);
    }
}