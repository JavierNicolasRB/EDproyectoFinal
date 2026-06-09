package finalproyecto;

/**
 * Clase Principal que sirve como punto de entrada de la aplicación.
 * Sigue el patrón de diseño MVC (Modelo-Vista-Controlador).
 */
public class Principal {
	
	/**
	 * Método principal que inicializa y conecta el Modelo, la Vista y el Controlador.
	 * * @param args Argumentos de la línea de comandos.
	 */
	public static void main(String[] args) {
		Modelo modelo = new Modelo();
		Vista vista = new Vista();
		new Controlador(vista, modelo);
	}
}