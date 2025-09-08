package pedidos.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pedidos.models.Conexion;
import pedidos.models.Cliente;
import pedidos.models.Producto;
import pedidos.models.Pedido;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/PedidoServlet")
public class PedidoServlet extends HttpServlet {
	Conexion conexion = new Conexion();
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		try (Connection con = conexion.getConnection()) {
			PreparedStatement ps;
			switch (action) {
			case "addCliente":
				String nombreC = request.getParameter("nombreCliente");
				String email = request.getParameter("emailCliente");
				ps = con.prepareStatement("INSERT INTO clientes (nombre, email) VALUES (?,?)");
				ps.setString(1, nombreC);
				ps.setString(2, email);
				ps.executeUpdate();
				break;
			case "addProducto":
				String nombreP = request.getParameter("nombreProducto");
				double precio = Double.parseDouble(request.getParameter("precioProducto"));
				ps = con.prepareStatement("INSERT INTO productos (nombre, precio) VALUES (?, ?)");
				ps.setString(1, nombreP);
				ps.setDouble(2, precio);
				ps.executeUpdate();
				break;
			case "addPedido":
				int clienteId = Integer.parseInt(request.getParameter("clienteId"));
				int productoId = Integer.parseInt(request.getParameter("productoId"));
				int cantidad = Integer.parseInt(request.getParameter("cantidad"));
				ps = con.prepareStatement("INSERT INTO pedidos (cliente_id, producto_id, cantidad) VALUES (?, ?, ?)");
				ps.setInt(1, clienteId);
				ps.setInt(2, productoId);
				ps.setInt(3, cantidad);
				ps.executeUpdate();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Llama a doGet para cargar los datos actualizados y redirigir a index.jsp
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection con = conexion.getConnection()) {
			// Lista de clientes
			List<Cliente> clientes = new ArrayList<>();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM clientes");
			while (rs.next()) {
				Cliente c = new Cliente();
				c.setId(rs.getInt("id"));
				c.setNombre(rs.getString("nombre"));
				c.setEmail(rs.getString("email"));
				clientes.add(c);
			}
			request.setAttribute("clientes", clientes);

			// Lista de productos
			List<Producto> productos = new ArrayList<>();
			rs = st.executeQuery("SELECT * FROM productos");
			while (rs.next()) {
				Producto p = new Producto();
				p.setId(rs.getInt("id"));
				p.setNombre(rs.getString("nombre"));
				p.setPrecio(rs.getDouble("precio"));
				productos.add(p);
			}
			request.setAttribute("productos", productos);

			// Lista de pedidos
			List<Pedido> pedidos = new ArrayList<>();
			rs = st.executeQuery("SELECT * FROM pedidos");
			while (rs.next()) {
				Pedido ped = new Pedido();
				ped.setId(rs.getInt("id"));
				ped.setClienteId(rs.getInt("cliente_id"));
				ped.setProductold(rs.getInt("producto_id"));
				ped.setCantidad(rs.getInt("cantidad"));
				pedidos.add(ped);
			}
			request.setAttribute("pedidos", pedidos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}