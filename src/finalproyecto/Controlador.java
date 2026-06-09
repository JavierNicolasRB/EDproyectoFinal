package finalproyecto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Button;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Clase Controlador que gestiona la lógica de negocio y las interacciones del usuario.
 * Escucha los botones de la interfaz gráfica y se comunica con la base de datos.
 */
public class Controlador implements ActionListener, WindowListener {
	private Vista vista;
	private Modelo modelo;

	/**
	 * Constructor del Controlador.
	 * Asigna los escuchadores (listeners) a los botones y a la ventana.
	 * * @param vista  Referencia a la interfaz gráfica.
	 * @param modelo Referencia al gestor de base de datos.
	 */
	public Controlador(Vista vista, Modelo modelo) {
		this.vista = vista;
		this.modelo = modelo;

		// --- Escuchadores para los botones de navegación ---
		this.vista.btnVerEventosMenu.addActionListener(this);
		this.vista.btnSacarTicketMenu.addActionListener(this);
		this.vista.btnVolver.addActionListener(this);

		// Escuchadores originales
		this.vista.btnVerEventos.addActionListener(this);
		this.vista.btnComprar.addActionListener(this);
		this.vista.ventana.addWindowListener(this);
	}

	/**
	 * Define las acciones a ejecutar dependiendo del botón que el usuario haya pulsado.
	 * Gestiona tanto la navegación entre paneles como las inserciones en base de datos.
	 * * @param e Evento capturado al pulsar un botón.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String botonPulsado = ((Button) e.getSource()).getLabel();

		// --- GESTIÓN DE NAVEGACIÓN ---
		if (botonPulsado.equals("Ver Eventos")) {
			vista.ventana.remove(vista.panelMenu);
			vista.ventana.remove(vista.panelFormulario);
			vista.panelEventos.add(vista.areaTexto, java.awt.BorderLayout.CENTER); 
			vista.ventana.add(vista.panelEventos, java.awt.BorderLayout.CENTER);
			
			vista.ventana.validate();
			vista.ventana.repaint();
			
			cargarEventos();
		} 
		else if (botonPulsado.equals("Sacar Ticket")) {
			vista.areaTexto.setText(""); 
			
			vista.ventana.remove(vista.panelMenu);
			vista.ventana.remove(vista.panelEventos);
			vista.ventana.add(vista.areaTexto, java.awt.BorderLayout.SOUTH); 
			vista.ventana.add(vista.panelFormulario, java.awt.BorderLayout.CENTER);
			
			vista.ventana.validate();
			vista.ventana.repaint();
		} 
		else if (botonPulsado.equals("Volver al Menú")) {
			vista.areaTexto.setText("");
			
			vista.ventana.remove(vista.panelFormulario);
			vista.ventana.remove(vista.panelEventos);
			vista.ventana.add(vista.areaTexto, java.awt.BorderLayout.SOUTH);
			vista.ventana.add(vista.panelMenu, java.awt.BorderLayout.CENTER);
			
			vista.ventana.validate();
			vista.ventana.repaint();
		}

		// --- GESTIÓN DE COMPRAS Y EVENTOS ---
		else if (botonPulsado.equals("Ver Eventos Disponibles")) {
			cargarEventos();
		} 
		else if (botonPulsado.equals("Confirmar y Comprar Ticket")) {
			try {
				String nombre = vista.txtNombre.getText();
				String dniEmail = vista.txtDni.getText();
				int idEvento = Integer.parseInt(vista.txtEvento.getText());
				int idTipo = Integer.parseInt(vista.txtTipo.getText());
				int cantidad = Integer.parseInt(vista.txtCantidad.getText());

				double precioBase = (idTipo == 1) ? 25.00 : 60.00;
				double total = cantidad * precioBase;

				Connection conn = modelo.obtenerConexion();
				String sqlInsert = "INSERT INTO ventas (nombreClienteVenta, dniEmailClienteVenta, cantidadTicketsVenta, totalPagadoVenta, idEventoFK, idTipoFK) " 
				+ "VALUES ('" + nombre + "', '" + dniEmail + "', " + cantidad + ", " + total + ", " + idEvento + ", " + idTipo + ")";

				PreparedStatement stmt = conn.prepareStatement(sqlInsert);
				stmt.executeUpdate();
				stmt.close();
				conn.close();

				vista.areaTexto.setText("¡COMPRA CORRECTA!\n\n" + "Cliente: " + nombre + "\n" + "Entradas: " + cantidad + "\n" + "Total Cargado: " + total + "€");

				vista.txtNombre.setText("");
				vista.txtDni.setText("");
				vista.txtEvento.setText("");
				vista.txtTipo.setText("");
				vista.txtCantidad.setText("");

			} catch (Exception ex) {
				vista.areaTexto.setText("Error en los datos introducidos: " + ex.getMessage());
			}
		}
	}

	/**
	 * Método auxiliar que ejecuta una consulta SELECT a la base de datos 
	 * para extraer los eventos disponibles y mostrarlos en el área de texto.
	 */
	private void cargarEventos() {
		try {
			Connection conn = modelo.obtenerConexion();
			String sql = "SELECT * FROM eventos";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			String resultadoText = "--- EVENTOS DISPONIBLES ---\n";
			while (rs.next()) {
				resultadoText += "ID: " + rs.getInt("idEvento") + " - " + rs.getString("nombreEvento") + "\n";
				resultadoText += "Cartel: " + rs.getString("cartelEvento") + "\n\n";
			}

			vista.areaTexto.setText(resultadoText);

			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception ex) {
			vista.areaTexto.setText("Error al cargar eventos: " + ex.getMessage());
		}
	}

	// --- MÉTODOS DE WINDOWLISTENER ---
	
	/**
	 * Cierra la aplicación cuando el usuario pulsa la "X" de la ventana.
	 */
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}