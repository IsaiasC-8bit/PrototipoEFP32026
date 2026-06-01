/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Carreras;
import Controlador.Carreras.clscarreras;
import Controlador.Compras.clsFacturascompras;
import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Controlador.clsUsuarioConectado;
import Modelo.BitacoraDAO;
/**
 *
 * @author isaia
 */
public class carrerasDAO {
        private void registrarBitacora(String accion) {

        int usuario = clsUsuarioConectado.getUsuId();

        if (usuario == 0) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        BitacoraDAO bitacora = new BitacoraDAO();

        int aplCodigoBitacora = 1000;

        bitacora.insert(usuario, aplCodigoBitacora, accion);
    }
        
        
       public List<clscarreras> listar() {

        List<clscarreras> lista = new ArrayList<>();

        String sql =
                "SELECT codigo_carrera, nombre_carrera, codigo_facultad, estatus_carrera, "
              + "FROM carreras";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                clscarreras carrera = new clscarreras();

                carrera.setCodigo_carrera(rs.getInt("codigo_carrera"));
                carrera.setNombre_carrera(rs.getString("nombre_carrera"));
                carrera.setCodigo_facultad(rs.getString("codigo_facultad"));
                carrera.setEstatus_carrera(rs.getString("estatus_carrera"));
                lista.add(carrera);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al listar facturas", e);
        }

        return lista;
    }
     
     
      public boolean insert(clscarreras carrera) {

        String sql =
                "INSERT INTO carreras"
              + "(codigo_carrera,nombre_carrera,codigo_facultad,estatus_carrera) "
              + "VALUES (?,?,?,?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, carrera.getCodigo_carrera());
            ps.setString(2, carrera.getNombre_carrera());
            ps.setString(3, carrera.getCodigo_facultad());
            ps.setString(4, carrera.getEstatus_carrera());
            boolean resultado = ps.executeUpdate() > 0;
            if (resultado) {
                registrarBitacora("Insertó una nueva carrera: " + carrera.getNombre_carrera());
            }

            return resultado;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
      }
          public void update(clscarreras carrera) {

        String sql =
                "UPDATE carreras SET "
              + "codigo_carrera=?, "
              + "nombre_carrera=?, "
              + "codigo_facultad=?, "
              + "estatus_carrera=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, carrera.getCodigo_carrera());
            ps.setString(2, carrera.getNombre_carrera());
            ps.setString(3, carrera.getCodigo_facultad());
            ps.setString(4, carrera.getEstatus_carrera());
            
            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("No se encontró la factura");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar factura", e);
        }
    }
    public void delete(int codigo_carreras) {

        String sql =
                "DELETE FROM carreras WHERE codigo_carrera=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo_carreras);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("No se encontró la factura");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar factura", e);
        }
    }    
    //busqueda por id
    public clscarreras query(int codigo_carreras) {

        clscarreras carreras = null;

        String sql =
                "SELECT * FROM facturascompras WHERE Faccomid=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo_carreras);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    carreras = new clscarreras();

                    carreras.setCodigo_carrera(rs.getInt("codigo_carrera"));
                    carreras.setNombre_carrera(rs.getString("nombre_carrera"));
                    carreras.setCodigo_facultad(rs.getString("codigo_facultad"));
                    carreras.setEstatus_carrera(rs.getString("estatus_carrera"));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al consultar factura", e);
        }

        return carreras;
    }
}
