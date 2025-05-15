package service;

import java.awt.Color;
import java.awt.HeadlessException;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import modelo.Empleado;
import modelo.REGISTRO_ENTRADAS_SALIDAS;
import modelo.Vehiculo;
import vista.vistaCLIENTE;
import vista.vistaEMPLEADO;
import vista.vistaENTRADA;
import vista.vistaRESULTADOS;
import vista.vistaSALIDA;
import vista.vistaVEHICULO;

public class Service {

    // Rutas base para los archivos .dat
    private static final String BASE_DATA_PATH = "SGEV_RAF_DATA" + File.separator;

    private static final String EMPLEADO_DAT_FILE = BASE_DATA_PATH + "EMPLEADO.dat";
    private static final String CLIENTE_DAT_FILE = BASE_DATA_PATH + "CLIENTE.dat";
    private static final String VEHICULO_DAT_FILE = BASE_DATA_PATH + "VEHICULO.dat";
    private static final String ENTRADA_DAT_FILE = BASE_DATA_PATH + "ENTRADA.dat";
    private static final String SALIDA_DAT_FILE = BASE_DATA_PATH + "SALIDA.dat";

    public Service() {
        File dataDir = new File(BASE_DATA_PATH);
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                System.out.println("Directorio de datos creado: " + dataDir.getAbsolutePath());
            } else {
                System.err.println("Error al crear el directorio de datos: " + dataDir.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Error crítico: No se pudo crear el directorio de datos.", "Error de Directorio", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- Métodos Auxiliares para RandomAccessFile ---
    private void writeString(RandomAccessFile raf, String str, String marcadorSiNulo) throws IOException {
        if (str == null || str.trim().isEmpty()) {
            raf.writeUTF(marcadorSiNulo);
        } else {
            raf.writeUTF(str.trim());
        }
    }

    private String readString(RandomAccessFile raf, String marcadorSiNulo, boolean devolverNuloSiMarcador) throws IOException {
        String str = raf.readUTF();
        if (str.equals(marcadorSiNulo)) {
            return devolverNuloSiMarcador ? null : marcadorSiNulo;
        }
        return str;
    }

    //---------------------------------------------------EMPLEADOS (CON RANDOM ACCESS FILE)---------------------------------------------------------------------------------
    public void anadirEMPLEADO_RAF(Empleado emp) {
        File archivoDat = new File(EMPLEADO_DAT_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            raf.seek(raf.length());
            raf.writeBoolean(true); // activo
            raf.writeInt(emp.getIdEmpleado());
            raf.writeInt(emp.getNacionalidad());
            writeString(raf, emp.getDni(), "-");
            writeString(raf, emp.getCarnetExtranjeria(), "-");
            writeString(raf, emp.getPasaporte(), "-");
            writeString(raf, emp.getResidencia(), "-");
            writeString(raf, emp.getN_A(), "Sin Nombre");
            raf.writeInt(emp.getSexo());
            writeString(raf, emp.getFecha_nacimiento(), "00/00/0000");
            writeString(raf, emp.getTelefono(), "-");
            writeString(raf, emp.getCorreo(), "-");
            writeString(raf, emp.getDireccion(), "-");
            writeString(raf, emp.getDistrito(), "-");
            JOptionPane.showMessageDialog(null, "Empleado añadido correctamente (RAF).");
        } catch (IOException ex) {
            System.err.println("Error al añadir empleado (RAF): " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo registrar el empleado (RAF).\n" + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void leerEMPLEADOS_RAF(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);
        File archivoDat = new File(EMPLEADO_DAT_FILE);
        if (!archivoDat.exists()) {
            System.out.println("Archivo " + EMPLEADO_DAT_FILE + " no existe.");
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idEmpleado = raf.readInt();
                int nacionalidad = raf.readInt();
                String dni = readString(raf, "-", false);
                String carnetExtranjeria = readString(raf, "-", false);
                String pasaporte = readString(raf, "-", false);
                String residencia = readString(raf, "-", false);
                String n_a = readString(raf, "Sin Nombre", false);
                int sexo = raf.readInt();
                String fecha_nacimiento = readString(raf, "00/00/0000", false);
                String telefono = readString(raf, "-", false);
                String correo = readString(raf, "-", false);
                String direccion = readString(raf, "-", false);
                String distrito = readString(raf, "-", false);
                if (activo) {
                    modeloTabla.addRow(new Object[]{
                        idEmpleado, nacionalidad, dni, carnetExtranjeria, pasaporte,
                        residencia, n_a, sexo, fecha_nacimiento, telefono,
                        correo, direccion, distrito
                    });
                }
            }
        } catch (EOFException e) {
            System.out.println("Fin de archivo alcanzado al leer empleados (RAF).");
        } catch (IOException ex) {
            System.err.println("Error al leer empleados (RAF): " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la lista de empleados (RAF).\n" + ex.getMessage(), "Error de Lectura", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean verificarIdLibre_EMPLEADO_RAF(int idVerificar) {
        File archivoDat = new File(EMPLEADO_DAT_FILE);
        if (!archivoDat.exists()) {
            return true;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                // Saltar el resto de los campos
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "Sin Nombre", false);
                raf.readInt();
                readString(raf, "00/00/0000", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                if (idActual == idVerificar && activo) {
                    return false;
                }
            }
        } catch (EOFException e) {
            /* Fin de archivo */ } catch (IOException ex) {
            System.err.println("Error al verificar ID de empleado (RAF): " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean actualizarEMPLEADO_RAF(Empleado empActualizado) {
        File archivoDat = new File(EMPLEADO_DAT_FILE);
        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo de empleados no encontrado para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        long posicionRegistro = -1;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long inicioRegistroActual = raf.getFilePointer();
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                if (idActual == empActualizado.getIdEmpleado() && activo) {
                    posicionRegistro = inicioRegistroActual;
                    break;
                }
                // Saltar campos
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "Sin Nombre", false);
                raf.readInt();
                readString(raf, "00/00/0000", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
            }
            if (posicionRegistro != -1) {
                raf.seek(posicionRegistro);
                raf.writeBoolean(true);
                raf.writeInt(empActualizado.getIdEmpleado());
                raf.writeInt(empActualizado.getNacionalidad());
                writeString(raf, empActualizado.getDni(), "-");
                writeString(raf, empActualizado.getCarnetExtranjeria(), "-");
                writeString(raf, empActualizado.getPasaporte(), "-");
                writeString(raf, empActualizado.getResidencia(), "-");
                writeString(raf, empActualizado.getN_A(), "Sin Nombre");
                raf.writeInt(empActualizado.getSexo());
                writeString(raf, empActualizado.getFecha_nacimiento(), "00/00/0000");
                writeString(raf, empActualizado.getTelefono(), "-");
                writeString(raf, empActualizado.getCorreo(), "-");
                writeString(raf, empActualizado.getDireccion(), "-");
                writeString(raf, empActualizado.getDistrito(), "-");
                JOptionPane.showMessageDialog(null, "Empleado actualizado correctamente (RAF).");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Empleado con ID " + empActualizado.getIdEmpleado() + " no encontrado o inactivo (RAF).", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error al actualizar empleado (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al actualizar empleado (RAF).\n" + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean eliminarEMPLEADO_RAF(int idEliminar) {
        File archivoDat = new File(EMPLEADO_DAT_FILE);
        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo de empleados no encontrado para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        long posicionRegistro = -1;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long inicioRegistroActual = raf.getFilePointer();
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                if (idActual == idEliminar && activo) {
                    posicionRegistro = inicioRegistroActual;
                    break;
                }
                // Saltar campos
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "Sin Nombre", false);
                raf.readInt();
                readString(raf, "00/00/0000", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
            }
            if (posicionRegistro != -1) {
                raf.seek(posicionRegistro);
                raf.writeBoolean(false);
                JOptionPane.showMessageDialog(null, "Empleado con ID " + idEliminar + " marcado como inactivo (RAF).");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Empleado con ID " + idEliminar + " no encontrado o ya inactivo (RAF).", "Información", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error al eliminar empleado (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al eliminar empleado (RAF).\n" + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public Empleado buscarEmpleadoPorId_RAF(int idBuscar) {
        File archivoDat = new File(EMPLEADO_DAT_FILE);
        if (!archivoDat.exists()) {
            return null;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idEmpleado = raf.readInt();
                int nacionalidad = raf.readInt();
                String dni = readString(raf, "-", true);
                String carnetExtranjeria = readString(raf, "-", true);
                String pasaporte = readString(raf, "-", true);
                String residencia = readString(raf, "-", false);
                String n_a = readString(raf, "Sin Nombre", false);
                int sexo = raf.readInt();
                String fecha_nacimiento = readString(raf, "00/00/0000", false);
                String telefono = readString(raf, "-", false);
                String correo = readString(raf, "-", false);
                String direccion = readString(raf, "-", false);
                String distrito = readString(raf, "-", false);
                if (idEmpleado == idBuscar && activo) {
                    return new Empleado(idEmpleado, nacionalidad, dni, carnetExtranjeria, pasaporte,
                            residencia, n_a, sexo, fecha_nacimiento, telefono,
                            correo, direccion, distrito);
                }
            }
        } catch (EOFException e) {
            /* Fin de archivo */ } catch (IOException ex) {
            System.err.println("Error al buscar empleado por ID (RAF): " + ex.getMessage());
        }
        return null;
    }

    public void verificarEMPLEADOS(vistaEMPLEADO vemp) {
        int selectedIndex = vemp.cbxNACIONALIDAD.getSelectedIndex();
        boolean esModoActualizar = vemp.btnREGISTRAR.getText().equals("ACTUALIZAR");
        vemp.jlDNI.setVisible(false);
        vemp.txtDNI.setVisible(false);
        vemp.jlRESIDENTE.setVisible(false);
        vemp.rbtnSI.setVisible(false);
        vemp.rbtnNO.setVisible(false);
        vemp.jlCARNET.setVisible(false);
        vemp.jlPASAPORTE.setVisible(false);
        vemp.txtEXTRANJERIA.setVisible(false);
        vemp.txtPASAPORTE.setVisible(false);
        switch (selectedIndex) {
            case 1:
                vemp.jlDNI.setVisible(true);
                vemp.txtDNI.setVisible(true);
                if (!esModoActualizar) {
                    vemp.btnGRP_ASOCIACION.clearSelection();
                    vemp.txtEXTRANJERIA.setText("");
                    vemp.txtPASAPORTE.setText("");
                }
                break;
            case 2:
                vemp.jlRESIDENTE.setVisible(true);
                vemp.rbtnSI.setVisible(true);
                vemp.rbtnNO.setVisible(true);
                if (!esModoActualizar) {
                    vemp.txtDNI.setText("");
                }
                radioBOTONES(vemp);
                break;
            default:
                if (!esModoActualizar) {
                    vemp.btnGRP_ASOCIACION.clearSelection();
                    vemp.txtDNI.setText("");
                    vemp.txtEXTRANJERIA.setText("");
                    vemp.txtPASAPORTE.setText("");
                }
                break;
        }
    }

    public void radioBOTONES(vistaEMPLEADO vemp) {
        boolean esModoActualizar = vemp.btnREGISTRAR.getText().equals("ACTUALIZAR");
        if (vemp.rbtnSI.isSelected()) {
            vemp.jlCARNET.setVisible(true);
            vemp.txtEXTRANJERIA.setVisible(true);
            vemp.jlPASAPORTE.setVisible(false);
            vemp.txtPASAPORTE.setVisible(false);
            if (!esModoActualizar) {
                vemp.txtPASAPORTE.setText("");
            }
        } else if (vemp.rbtnNO.isSelected()) {
            vemp.jlPASAPORTE.setVisible(true);
            vemp.txtPASAPORTE.setVisible(true);
            vemp.jlCARNET.setVisible(false);
            vemp.txtEXTRANJERIA.setVisible(false);
            if (!esModoActualizar) {
                vemp.txtEXTRANJERIA.setText("");
            }
        } else {
            vemp.jlCARNET.setVisible(false);
            vemp.txtEXTRANJERIA.setVisible(false);
            vemp.jlPASAPORTE.setVisible(false);
            vemp.txtPASAPORTE.setVisible(false);
            if (!esModoActualizar) {
                vemp.txtEXTRANJERIA.setText("");
                vemp.txtPASAPORTE.setText("");
            }
        }
    }

    public void verificarCAMPOS(vistaEMPLEADO vemp) {
        try {
            if (vemp.txtID_EMPLEADO.getText().isEmpty() || vemp.cbxNACIONALIDAD.getSelectedIndex() == -1
                    || vemp.txtN_A.getText().isEmpty() || vemp.cbxSEXO.getSelectedIndex() == -1
                    || !verificarFECHA(vemp.txtFECHA) || vemp.txtTELEFONO.getText().isEmpty()
                    || vemp.txtCORREO.getText().isEmpty() || vemp.txtDIRECCION.getText().isEmpty()
                    || vemp.txtDISTRITO.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "NO DEJE CAMPOS VACIOS o corrija formatos");
                return;
            }
            if (!esMayorDeEdad(vemp.txtFECHA.getText())) {
                JOptionPane.showMessageDialog(null, "El empleado debe ser mayor de 18 años.", "Edad Inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int idEmpleado;
            try {
                idEmpleado = Integer.parseInt(vemp.txtID_EMPLEADO.getText());
                if (idEmpleado < 0) {
                    JOptionPane.showMessageDialog(null, "El ID del empleado debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El ID del empleado debe ser un número entero.");
                return;
            }
            if (!verificarIdLibre_EMPLEADO_RAF(idEmpleado)) {
                JOptionPane.showMessageDialog(null, "El ID de empleado '" + idEmpleado + "' ya existe.", "ID No Disponible", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int nacionalidad = vemp.cbxNACIONALIDAD.getSelectedIndex();
            String dni = (nacionalidad == 1 && !vemp.txtDNI.getText().isEmpty()) ? vemp.txtDNI.getText() : "-";
            String carnet = (nacionalidad == 2 && vemp.rbtnSI.isSelected() && !vemp.txtEXTRANJERIA.getText().isEmpty()) ? vemp.txtEXTRANJERIA.getText() : "-";
            String pasaporte = (nacionalidad == 2 && vemp.rbtnNO.isSelected() && !vemp.txtPASAPORTE.getText().isEmpty()) ? vemp.txtPASAPORTE.getText() : "-";
            String residencia = "-";
            if (nacionalidad == 1) {
                residencia = "Si";
            } else if (nacionalidad == 2) {
                residencia = vemp.rbtnSI.isSelected() ? "Si" : (vemp.rbtnNO.isSelected() ? "No" : "-");
            }
            String n_a = vemp.txtN_A.getText();
            int sexo = vemp.cbxSEXO.getSelectedIndex();
            String fechaNac = vemp.txtFECHA.getText();
            String telefono = vemp.txtTELEFONO.getText();
            String correo = vemp.txtCORREO.getText();
            String direccion = vemp.txtDIRECCION.getText();
            String distrito = vemp.txtDISTRITO.getText();
            if (nacionalidad == 1 && dni.equals("-")) {
                JOptionPane.showMessageDialog(null, "Ingrese DNI para nacionalidad Peruana.");
                return;
            }
            if (nacionalidad == 2) {
                if (residencia.equals("-")) {
                    JOptionPane.showMessageDialog(null, "Seleccione si es Residente o No para nacionalidad Extranjera.");
                    return;
                }
                if (residencia.equals("Si") && carnet.equals("-")) {
                    JOptionPane.showMessageDialog(null, "Ingrese Carnet de Extranjería para residentes.");
                    return;
                }
                if (residencia.equals("No") && pasaporte.equals("-")) {
                    JOptionPane.showMessageDialog(null, "Ingrese Pasaporte para no residentes.");
                    return;
                }
            }
            Empleado nuevoEmpleado = new Empleado(idEmpleado, nacionalidad, dni, carnet, pasaporte, residencia, n_a, sexo, fechaNac, telefono, correo, direccion, distrito);
            anadirEMPLEADO_RAF(nuevoEmpleado);
            //limpiarEMPLEADO(vemp);
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(null, "Error en verificarCAMPOS Empleado (RAF): " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public boolean procesarActualizacionEmpleado(vistaEMPLEADO vemp) {
        if (vemp.cbxNACIONALIDAD.getSelectedIndex() == -1
                || vemp.txtN_A.getText().isEmpty() || vemp.cbxSEXO.getSelectedIndex() == -1
                || !verificarFECHA(vemp.txtFECHA) || !esMayorDeEdad(vemp.txtFECHA.getText())
                || vemp.txtTELEFONO.getText().isEmpty() || vemp.txtCORREO.getText().isEmpty()
                || vemp.txtDIRECCION.getText().isEmpty() || vemp.txtDISTRITO.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vemp, "Revise los campos obligatorios y el formato/validez de la fecha de nacimiento.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int idActualizar = Integer.parseInt(vemp.txtID_EMPLEADO.getText());
        int nacionalidad = vemp.cbxNACIONALIDAD.getSelectedIndex();
        String dni = (nacionalidad == 1 && !vemp.txtDNI.getText().isEmpty()) ? vemp.txtDNI.getText() : "-";
        String carnet = (nacionalidad == 2 && vemp.rbtnSI.isSelected() && !vemp.txtEXTRANJERIA.getText().isEmpty()) ? vemp.txtEXTRANJERIA.getText() : "-";
        String pasaporte = (nacionalidad == 2 && vemp.rbtnNO.isSelected() && !vemp.txtPASAPORTE.getText().isEmpty()) ? vemp.txtPASAPORTE.getText() : "-";
        String residencia = "-";
        if (nacionalidad == 1) {
            residencia = "Si";
        } else if (nacionalidad == 2) {
            residencia = vemp.rbtnSI.isSelected() ? "Si" : (vemp.rbtnNO.isSelected() ? "No" : "-");
        }
        String n_a = vemp.txtN_A.getText();
        int sexo = vemp.cbxSEXO.getSelectedIndex();
        String fechaNac = vemp.txtFECHA.getText();
        String telefono = vemp.txtTELEFONO.getText();
        String correo = vemp.txtCORREO.getText();
        String direccion = vemp.txtDIRECCION.getText();
        String distrito = vemp.txtDISTRITO.getText();
        if (nacionalidad == 1 && dni.equals("-")) {
            JOptionPane.showMessageDialog(vemp, "Ingrese DNI para nacionalidad Peruana.");
            return false;
        }
        if (nacionalidad == 2) {
            if (residencia.equals("-")) {
                JOptionPane.showMessageDialog(vemp, "Seleccione si es Residente o No para nacionalidad Extranjera.");
                return false;
            }
            if (residencia.equals("Si") && carnet.equals("-")) {
                JOptionPane.showMessageDialog(vemp, "Ingrese Carnet de Extranjería para residentes.");
                return false;
            }
            if (residencia.equals("No") && pasaporte.equals("-")) {
                JOptionPane.showMessageDialog(vemp, "Ingrese Pasaporte para no residentes.");
                return false;
            }
        }
        Empleado empleadoActualizado = new Empleado(idActualizar, nacionalidad, dni, carnet, pasaporte, residencia, n_a, sexo, fechaNac, telefono, correo, direccion, distrito);
        return actualizarEMPLEADO_RAF(empleadoActualizado);
    }

    public void disenoTABLA_EMPLEADOS(JTable tablaEMPLEADOS) {
        DefaultTableModel modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("idEmpleado");
        modeloTabla.addColumn("Nacionalidad");
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Carnet Ext.");
        modeloTabla.addColumn("Pasaporte");
        modeloTabla.addColumn("Residencia");
        modeloTabla.addColumn("Nombres y Apell.");
        modeloTabla.addColumn("Sexo");
        modeloTabla.addColumn("Fecha Nac.");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Correo");
        modeloTabla.addColumn("Dirección");
        modeloTabla.addColumn("Distrito");
        leerEMPLEADOS_RAF(modeloTabla);
        tablaEMPLEADOS.setModel(modeloTabla);
    }

    public void limpiarEMPLEADO(vistaEMPLEADO vemp) {
        vemp.txtID_EMPLEADO.setText("");
        vemp.cbxNACIONALIDAD.setSelectedIndex(-1);
        vemp.txtN_A.setText("");
        vemp.cbxSEXO.setSelectedIndex(-1);
        vemp.txtFECHA.setText("");
        vemp.txtTELEFONO.setText("");
        vemp.txtCORREO.setText("");
        vemp.txtDIRECCION.setText("");
        vemp.txtDISTRITO.setText("");
        vemp.txtDNI.setText("");
        vemp.txtEXTRANJERIA.setText("");
        vemp.txtPASAPORTE.setText("");
        vemp.btnGRP_ASOCIACION.clearSelection();
        vemp.txtID_EMPLEADO.setEditable(true);
        vemp.txtID_EMPLEADO.setBackground(Color.WHITE);
        vemp.btnREGISTRAR.setText("REGISTRAR");
        vemp.jl_EMPLEADO.setText("Registro de Empleado");
        vemp.setTitle("Registro de Empleado");
        vemp.jlDNI.setVisible(false);
        vemp.txtDNI.setVisible(false);
        vemp.jlRESIDENTE.setVisible(false);
        vemp.rbtnSI.setVisible(false);
        vemp.rbtnNO.setVisible(false);
        vemp.jlCARNET.setVisible(false);
        vemp.jlPASAPORTE.setVisible(false);
        vemp.txtEXTRANJERIA.setVisible(false);
        vemp.txtPASAPORTE.setVisible(false);
    }

    //---------------------------------------------------CLIENTES (CON RANDOM ACCESS FILE)--------------------------------------------------------------------------------------------------------
    public void anadirCLIENTE_RAF(Cliente clt) {
        File archivoDat = new File(CLIENTE_DAT_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            raf.seek(raf.length());
            raf.writeBoolean(true);
            raf.writeInt(clt.getIdCliente());
            raf.writeInt(clt.getNacionalidad());
            writeString(raf, clt.getDni(), "-");
            writeString(raf, clt.getCarnetExtranjeria(), "-");
            writeString(raf, clt.getPasaporte(), "-");
            writeString(raf, clt.getResidencia(), "-");
            writeString(raf, clt.getN_A(), "Sin Nombre");
            raf.writeInt(clt.getSexo());
            writeString(raf, clt.getNro_licencia(), "-");
            writeString(raf, clt.getTelefono(), "-");
            writeString(raf, clt.getCorreo(), "-");
            writeString(raf, clt.getDireccion(), "-");
            writeString(raf, clt.getDistrito(), "-");
            JOptionPane.showMessageDialog(null, "Cliente añadido correctamente (RAF).");
        } catch (IOException ex) {
            System.err.println("Error al añadir cliente (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "No se pudo registrar el cliente (RAF).\n" + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void leerCLIENTES_RAF(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);
        File archivoDat = new File(CLIENTE_DAT_FILE);
        if (!archivoDat.exists()) {
            System.out.println("Archivo " + CLIENTE_DAT_FILE + " no existe.");
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idCliente = raf.readInt();
                int nacionalidad = raf.readInt();
                String dni = readString(raf, "-", false);
                String carnetExtranjeria = readString(raf, "-", false);
                String pasaporte = readString(raf, "-", false);
                String residencia = readString(raf, "-", false);
                String n_a = readString(raf, "Sin Nombre", false);
                int sexo = raf.readInt();
                String nro_licencia = readString(raf, "-", false);
                String telefono = readString(raf, "-", false);
                String correo = readString(raf, "-", false);
                String direccion = readString(raf, "-", false);
                String distrito = readString(raf, "-", false);
                if (activo) {
                    modeloTabla.addRow(new Object[]{
                        idCliente, nacionalidad, dni, carnetExtranjeria, pasaporte,
                        residencia, n_a, sexo, nro_licencia, telefono,
                        correo, direccion, distrito
                    });
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al leer clientes (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al cargar la lista de clientes (RAF).\n" + ex.getMessage(), "Error de Lectura", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean verificarIdLibre_CLIENTE_RAF(int idVerificar) {
        File archivoDat = new File(CLIENTE_DAT_FILE);
        if (!archivoDat.exists()) {
            return true;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "Sin Nombre", false);
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                if (idActual == idVerificar && activo) {
                    return false;
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al verificar ID de cliente (RAF): " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean actualizarCLIENTE_RAF(Cliente cltActualizado) {
        File archivoDat = new File(CLIENTE_DAT_FILE);
        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo de clientes no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        long posicionRegistro = -1;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long inicioRegistroActual = raf.getFilePointer();
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                if (idActual == cltActualizado.getIdCliente() && activo) {
                    posicionRegistro = inicioRegistroActual;
                    break;
                }
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "Sin Nombre", false);
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
            }
            if (posicionRegistro != -1) {
                raf.seek(posicionRegistro);
                raf.writeBoolean(true);
                raf.writeInt(cltActualizado.getIdCliente());
                raf.writeInt(cltActualizado.getNacionalidad());
                writeString(raf, cltActualizado.getDni(), "-");
                writeString(raf, cltActualizado.getCarnetExtranjeria(), "-");
                writeString(raf, cltActualizado.getPasaporte(), "-");
                writeString(raf, cltActualizado.getResidencia(), "-");
                writeString(raf, cltActualizado.getN_A(), "Sin Nombre");
                raf.writeInt(cltActualizado.getSexo());
                writeString(raf, cltActualizado.getNro_licencia(), "-");
                writeString(raf, cltActualizado.getTelefono(), "-");
                writeString(raf, cltActualizado.getCorreo(), "-");
                writeString(raf, cltActualizado.getDireccion(), "-");
                writeString(raf, cltActualizado.getDistrito(), "-");
                JOptionPane.showMessageDialog(null, "Cliente actualizado (RAF).");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Cliente ID " + cltActualizado.getIdCliente() + " no encontrado o inactivo (RAF).", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error al actualizar cliente (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al actualizar cliente (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean eliminarCLIENTE_RAF(int idEliminar) {
        File archivoDat = new File(CLIENTE_DAT_FILE);
        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo de clientes no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        long posicionRegistro = -1;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long inicioRegistroActual = raf.getFilePointer();
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                if (idActual == idEliminar && activo) {
                    posicionRegistro = inicioRegistroActual;
                    break;
                }
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "Sin Nombre", false);
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
            }
            if (posicionRegistro != -1) {
                raf.seek(posicionRegistro);
                raf.writeBoolean(false);
                JOptionPane.showMessageDialog(null, "Cliente ID " + idEliminar + " marcado como inactivo (RAF).");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Cliente ID " + idEliminar + " no encontrado o ya inactivo (RAF).", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error al eliminar cliente (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al eliminar cliente (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public Cliente buscarClientePorId_RAF(int idBuscar) {
        File archivoDat = new File(CLIENTE_DAT_FILE);
        if (!archivoDat.exists()) {
            return null;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idCliente = raf.readInt();
                int nacionalidad = raf.readInt();
                String dni = readString(raf, "-", true);
                String carnetExtranjeria = readString(raf, "-", true);
                String pasaporte = readString(raf, "-", true);
                String residencia = readString(raf, "-", false);
                String n_a = readString(raf, "Sin Nombre", false);
                int sexo = raf.readInt();
                String nro_licencia = readString(raf, "-", false);
                String telefono = readString(raf, "-", false);
                String correo = readString(raf, "-", false);
                String direccion = readString(raf, "-", false);
                String distrito = readString(raf, "-", false);
                if (idCliente == idBuscar && activo) {
                    return new Cliente(idCliente, nacionalidad, dni, carnetExtranjeria, pasaporte, residencia,
                            n_a, sexo, nro_licencia, telefono, correo, direccion, distrito);
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al buscar cliente por ID (RAF): " + ex.getMessage());
        }
        return null;
    }

    public void verificarCLIENTES(vistaCLIENTE vclt) {
        int selectedIndex = vclt.cbxNACIONALIDAD.getSelectedIndex();
        boolean esModoActualizar = vclt.btnREGISTRAR.getText().equals("ACTUALIZAR");
        vclt.jlDNI.setVisible(false);
        vclt.txtDNI.setVisible(false);
        vclt.jlRESIDENTE.setVisible(false);
        vclt.rbtnSI.setVisible(false);
        vclt.rbtnNO.setVisible(false);
        vclt.jlCARNET.setVisible(false);
        vclt.jlPASAPORTE.setVisible(false);
        vclt.txtEXTRANJERIA.setVisible(false);
        vclt.txtPASAPORTE.setVisible(false);
        switch (selectedIndex) {
            case 1:
                vclt.jlDNI.setVisible(true);
                vclt.txtDNI.setVisible(true);
                if (!esModoActualizar) {
                    vclt.btnGRP_ASOCIACION.clearSelection();
                    vclt.txtEXTRANJERIA.setText("");
                    vclt.txtPASAPORTE.setText("");
                }
                break;
            case 2:
                vclt.jlRESIDENTE.setVisible(true);
                vclt.rbtnSI.setVisible(true);
                vclt.rbtnNO.setVisible(true);
                if (!esModoActualizar) {
                    vclt.txtDNI.setText("");
                }
                radioBOTONES_CLIENTE(vclt);
                break;
            default:
                if (!esModoActualizar) {
                    vclt.btnGRP_ASOCIACION.clearSelection();
                    vclt.txtDNI.setText("");
                    vclt.txtEXTRANJERIA.setText("");
                    vclt.txtPASAPORTE.setText("");
                }
                break;
        }
    }

    public void radioBOTONES_CLIENTE(vistaCLIENTE vclt) {
        boolean esModoActualizar = vclt.btnREGISTRAR.getText().equals("ACTUALIZAR");
        if (vclt.rbtnSI.isSelected()) {
            vclt.jlCARNET.setVisible(true);
            vclt.txtEXTRANJERIA.setVisible(true);
            vclt.jlPASAPORTE.setVisible(false);
            vclt.txtPASAPORTE.setVisible(false);
            if (!esModoActualizar) {
                vclt.txtPASAPORTE.setText("");
            }
        } else if (vclt.rbtnNO.isSelected()) {
            vclt.jlPASAPORTE.setVisible(true);
            vclt.txtPASAPORTE.setVisible(true);
            vclt.jlCARNET.setVisible(false);
            vclt.txtEXTRANJERIA.setVisible(false);
            if (!esModoActualizar) {
                vclt.txtEXTRANJERIA.setText("");
            }
        } else {
            vclt.jlCARNET.setVisible(false);
            vclt.txtEXTRANJERIA.setVisible(false);
            vclt.jlPASAPORTE.setVisible(false);
            vclt.txtPASAPORTE.setVisible(false);
            if (!esModoActualizar) {
                vclt.txtEXTRANJERIA.setText("");
                vclt.txtPASAPORTE.setText("");
            }
        }
    }

    public void verificarCAMPOS(vistaCLIENTE vclt) {
        try {
            if (vclt.txtID_CLIENTE.getText().isEmpty() || vclt.cbxNACIONALIDAD.getSelectedIndex() == -1
                    || vclt.txtN_A.getText().isEmpty() || vclt.cbxSEXO.getSelectedIndex() == -1
                    || vclt.txtLICENCIA.getText().isEmpty() || vclt.txtTELEFONO.getText().isEmpty()
                    || vclt.txtCORREO.getText().isEmpty() || vclt.txtDIRECCION.getText().isEmpty()
                    || vclt.txtDISTRITO.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "NO DEJE CAMPOS VACIOS");
                return;
            }
            int idCliente;
            try {
                idCliente = Integer.parseInt(vclt.txtID_CLIENTE.getText());
                if (idCliente < 0) {
                    JOptionPane.showMessageDialog(null, "El ID del cliente debe ser positivo.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El ID del cliente debe ser un número.");
                return;
            }

            if (!verificarIdLibre_CLIENTE_RAF(idCliente)) {
                JOptionPane.showMessageDialog(null, "El ID de cliente '" + idCliente + "' ya existe.", "ID No Disponible", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int nacionalidad = vclt.cbxNACIONALIDAD.getSelectedIndex();
            String dni = (nacionalidad == 1 && !vclt.txtDNI.getText().isEmpty()) ? vclt.txtDNI.getText() : "-";
            String carnet = (nacionalidad == 2 && vclt.rbtnSI.isSelected() && !vclt.txtEXTRANJERIA.getText().isEmpty()) ? vclt.txtEXTRANJERIA.getText() : "-";
            String pasaporte = (nacionalidad == 2 && vclt.rbtnNO.isSelected() && !vclt.txtPASAPORTE.getText().isEmpty()) ? vclt.txtPASAPORTE.getText() : "-";
            String residencia = "-";
            if (nacionalidad == 1) {
                residencia = "Si";
            } else if (nacionalidad == 2) {
                residencia = vclt.rbtnSI.isSelected() ? "Si" : (vclt.rbtnNO.isSelected() ? "No" : "-");
            }
            String n_a = vclt.txtN_A.getText();
            int sexo = vclt.cbxSEXO.getSelectedIndex();
            String nro_licencia = vclt.txtLICENCIA.getText();
            String telefono = vclt.txtTELEFONO.getText();
            String correo = vclt.txtCORREO.getText();
            String direccion = vclt.txtDIRECCION.getText();
            String distrito = vclt.txtDISTRITO.getText();
            if (nacionalidad == 1 && dni.equals("-")) {
                JOptionPane.showMessageDialog(null, "Ingrese DNI para nacionalidad Peruana.");
                return;
            }
            if (nacionalidad == 2) {
                if (residencia.equals("-")) {
                    JOptionPane.showMessageDialog(null, "Seleccione si es Residente o No para nacionalidad Extranjera.");
                    return;
                }
                if (residencia.equals("Si") && carnet.equals("-")) {
                    JOptionPane.showMessageDialog(null, "Ingrese Carnet de Extranjería para residentes.");
                    return;
                }
                if (residencia.equals("No") && pasaporte.equals("-")) {
                    JOptionPane.showMessageDialog(null, "Ingrese Pasaporte para no residentes.");
                    return;
                }
            }
            Cliente nuevoCliente = new Cliente(idCliente, nacionalidad, dni, carnet, pasaporte, residencia, n_a, sexo, nro_licencia, telefono, correo, direccion, distrito);
            anadirCLIENTE_RAF(nuevoCliente);
            //limpiarCLIENTE(vclt);
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(null, "Error en verificarCAMPOS Cliente (RAF): " + ex.getMessage());
        }
    }

    public boolean procesarActualizacionCliente(vistaCLIENTE vclt) {
        if (vclt.cbxNACIONALIDAD.getSelectedIndex() == -1 || vclt.txtN_A.getText().isEmpty()
                || vclt.cbxSEXO.getSelectedIndex() == -1 || vclt.txtLICENCIA.getText().isEmpty()
                || vclt.txtTELEFONO.getText().isEmpty() || vclt.txtCORREO.getText().isEmpty()
                || vclt.txtDIRECCION.getText().isEmpty() || vclt.txtDISTRITO.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vclt, "Revise los campos obligatorios.", "Error Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int idActualizar = Integer.parseInt(vclt.txtID_CLIENTE.getText());
        int nacionalidad = vclt.cbxNACIONALIDAD.getSelectedIndex();
        String dni = (nacionalidad == 1 && !vclt.txtDNI.getText().isEmpty()) ? vclt.txtDNI.getText() : "-";
        String carnet = (nacionalidad == 2 && vclt.rbtnSI.isSelected() && !vclt.txtEXTRANJERIA.getText().isEmpty()) ? vclt.txtEXTRANJERIA.getText() : "-";
        String pasaporte = (nacionalidad == 2 && vclt.rbtnNO.isSelected() && !vclt.txtPASAPORTE.getText().isEmpty()) ? vclt.txtPASAPORTE.getText() : "-";
        String residencia = "-";
        if (nacionalidad == 1) {
            residencia = "Si";
        } else if (nacionalidad == 2) {
            residencia = vclt.rbtnSI.isSelected() ? "Si" : (vclt.rbtnNO.isSelected() ? "No" : "-");
        }
        String n_a = vclt.txtN_A.getText();
        int sexo = vclt.cbxSEXO.getSelectedIndex();
        String nro_licencia = vclt.txtLICENCIA.getText();
        String telefono = vclt.txtTELEFONO.getText();
        String correo = vclt.txtCORREO.getText();
        String direccion = vclt.txtDIRECCION.getText();
        String distrito = vclt.txtDISTRITO.getText();
        if (nacionalidad == 1 && dni.equals("-")) {
            JOptionPane.showMessageDialog(vclt, "Ingrese DNI.");
            return false;
        }
        if (nacionalidad == 2) {
            if (residencia.equals("-")) {
                JOptionPane.showMessageDialog(vclt, "Seleccione Residencia.");
                return false;
            }
            if (residencia.equals("Si") && carnet.equals("-")) {
                JOptionPane.showMessageDialog(vclt, "Ingrese Carnet Extranjería.");
                return false;
            }
            if (residencia.equals("No") && pasaporte.equals("-")) {
                JOptionPane.showMessageDialog(vclt, "Ingrese Pasaporte.");
                return false;
            }
        }
        Cliente clienteActualizado = new Cliente(idActualizar, nacionalidad, dni, carnet, pasaporte, residencia, n_a, sexo, nro_licencia, telefono, correo, direccion, distrito);
        return actualizarCLIENTE_RAF(clienteActualizado);
    }

    public void disenoTABLA_CLIENTES(JTable tablaCLIENTES) {
        DefaultTableModel modeloTabla_2 = new DefaultTableModel();
        modeloTabla_2.addColumn("idCliente");
        modeloTabla_2.addColumn("Nacionalidad");
        modeloTabla_2.addColumn("DNI");
        modeloTabla_2.addColumn("Carnet Ext.");
        modeloTabla_2.addColumn("Pasaporte");
        modeloTabla_2.addColumn("Residencia");
        modeloTabla_2.addColumn("Nombres y Apell.");
        modeloTabla_2.addColumn("Sexo");
        modeloTabla_2.addColumn("Nro. Licencia");
        modeloTabla_2.addColumn("Teléfono");
        modeloTabla_2.addColumn("Correo");
        modeloTabla_2.addColumn("Dirección");
        modeloTabla_2.addColumn("Distrito");
        leerCLIENTES_RAF(modeloTabla_2);
        tablaCLIENTES.setModel(modeloTabla_2);
    }

    public void limpiarCLIENTE(vistaCLIENTE vclt) {
        vclt.txtID_CLIENTE.setText("");
        vclt.cbxNACIONALIDAD.setSelectedIndex(-1);
        vclt.txtN_A.setText("");
        vclt.cbxSEXO.setSelectedIndex(-1);
        vclt.txtLICENCIA.setText("");
        vclt.txtTELEFONO.setText("");
        vclt.txtCORREO.setText("");
        vclt.txtDIRECCION.setText("");
        vclt.txtDISTRITO.setText("");
        vclt.txtDNI.setText("");
        vclt.txtEXTRANJERIA.setText("");
        vclt.txtPASAPORTE.setText("");
        vclt.btnGRP_ASOCIACION.clearSelection();
        vclt.txtID_CLIENTE.setEditable(true);
        vclt.txtID_CLIENTE.setBackground(Color.WHITE);
        vclt.btnREGISTRAR.setText("REGISTRAR");
        vclt.jl_CLIENTE.setText("Registro de Cliente");
        vclt.setTitle("Registro de Cliente");
        vclt.jlDNI.setVisible(false);
        vclt.txtDNI.setVisible(false);
        vclt.jlRESIDENTE.setVisible(false);
        vclt.rbtnSI.setVisible(false);
        vclt.rbtnNO.setVisible(false);
        vclt.jlCARNET.setVisible(false);
        vclt.jlPASAPORTE.setVisible(false);
        vclt.txtEXTRANJERIA.setVisible(false);
        vclt.txtPASAPORTE.setVisible(false);
    }

    //----------------------------------------VISTA VEHICULO (CON RANDOM ACCESS FILE)----------------------------------
    public void anadirVEHICULO_RAF(Vehiculo vhcl) {
        File archivoDat = new File(VEHICULO_DAT_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            raf.seek(raf.length());
            raf.writeBoolean(true);
            raf.writeInt(vhcl.getIdVehiculo());
            writeString(raf, vhcl.getTipo(), "No especificado");
            writeString(raf, vhcl.getIdMARCA(), "Desconocida");
            writeString(raf, vhcl.getModelo(), "Desconocido");
            writeString(raf, vhcl.getColor(), "No especificado");
            writeString(raf, vhcl.getNroPLACA(), "-");
            writeString(raf, vhcl.getLunasPOLARIZADAS(), "NO");
            JOptionPane.showMessageDialog(null, "Vehículo añadido correctamente (RAF).");
        } catch (IOException ex) {
            System.err.println("Error al añadir vehículo (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "No se pudo registrar el vehículo (RAF).\n" + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void leerVEHICULOS_RAF(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);
        File archivoDat = new File(VEHICULO_DAT_FILE);
        if (!archivoDat.exists()) {
            System.out.println("Archivo " + VEHICULO_DAT_FILE + " no existe.");
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idVehiculo = raf.readInt();
                String tipo = readString(raf, "No especificado", false);
                String marca = readString(raf, "Desconocida", false);
                String modelo = readString(raf, "Desconocido", false);
                String color = readString(raf, "No especificado", false);
                String nroPlaca = readString(raf, "-", false);
                String lunasPolarizadas = readString(raf, "NO", false);
                if (activo) {
                    modeloTabla.addRow(new Object[]{
                        idVehiculo, tipo, marca, modelo, color, nroPlaca, lunasPolarizadas
                    });
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al leer vehículos (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al cargar lista de vehículos (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean verificarIdLibre_VEHICULO_RAF(int idVerificar) {
        File archivoDat = new File(VEHICULO_DAT_FILE);
        if (!archivoDat.exists()) {
            return true;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                if (idActual == idVerificar && activo) {
                    return false;
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al verificar ID de vehículo (RAF): " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean actualizarVEHICULO_RAF(Vehiculo vhclActualizado) {
        File archivoDat = new File(VEHICULO_DAT_FILE);
        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo de vehículos no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        long posicionRegistro = -1;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long inicioRegistroActual = raf.getFilePointer();
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                if (idActual == vhclActualizado.getIdVehiculo() && activo) {
                    posicionRegistro = inicioRegistroActual;
                    break;
                }
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
            }
            if (posicionRegistro != -1) {
                raf.seek(posicionRegistro);
                raf.writeBoolean(true);
                raf.writeInt(vhclActualizado.getIdVehiculo());
                writeString(raf, vhclActualizado.getTipo(), "No especificado");
                writeString(raf, vhclActualizado.getIdMARCA(), "Desconocida");
                writeString(raf, vhclActualizado.getModelo(), "Desconocido");
                writeString(raf, vhclActualizado.getColor(), "No especificado");
                writeString(raf, vhclActualizado.getNroPLACA(), "-");
                writeString(raf, vhclActualizado.getLunasPOLARIZADAS(), "NO");
                JOptionPane.showMessageDialog(null, "Vehículo actualizado (RAF).");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Vehículo ID " + vhclActualizado.getIdVehiculo() + " no encontrado o inactivo (RAF).", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error al actualizar vehículo (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al actualizar vehículo (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean eliminarVEHICULO_RAF(int idEliminar) {
        File archivoDat = new File(VEHICULO_DAT_FILE);
        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo de vehículos no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        long posicionRegistro = -1;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long inicioRegistroActual = raf.getFilePointer();
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                if (idActual == idEliminar && activo) {
                    posicionRegistro = inicioRegistroActual;
                    break;
                }
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
            }
            if (posicionRegistro != -1) {
                raf.seek(posicionRegistro);
                raf.writeBoolean(false);
                JOptionPane.showMessageDialog(null, "Vehículo ID " + idEliminar + " marcado como inactivo (RAF).");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Vehículo ID " + idEliminar + " no encontrado o ya inactivo (RAF).", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error al eliminar vehículo (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al eliminar vehículo (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public Vehiculo buscarVehiculoPorId_RAF(int idBuscar) {
        File archivoDat = new File(VEHICULO_DAT_FILE);
        if (!archivoDat.exists()) {
            return null;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idVehiculo = raf.readInt();
                String tipo = readString(raf, "No especificado", false);
                String marca = readString(raf, "Desconocida", false);
                String modelo = readString(raf, "Desconocido", false);
                String color = readString(raf, "No especificado", false);
                String nroPlaca = readString(raf, "-", false);
                String lunasPolarizadas = readString(raf, "NO", false);
                if (idVehiculo == idBuscar && activo) {
                    return new Vehiculo(idVehiculo, tipo, marca, modelo, color, nroPlaca, lunasPolarizadas);
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al buscar vehículo por ID (RAF): " + ex.getMessage());
        }
        return null;
    }

    public void verificarCBX_MARCA(JComboBox cbxMARCA, JComboBox cbxMODELO) {
        if (cbxMARCA.getSelectedIndex() == 0) {
            cbxMODELO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"YARIS", "COROLLA", "LEXUS"}));
        } else if (cbxMARCA.getSelectedIndex() == 1) {
            cbxMODELO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"SENTRA", "SUNNY", "TIDA", "VERSA"}));
        } else if (cbxMARCA.getSelectedIndex() == 2) {
            cbxMODELO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"CIVIC", "ACCORD", "CR-V"}));
        } else if (cbxMARCA.getSelectedIndex() == 3) {
            cbxMODELO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"FIESTA", "FOCUS", "ESCAPE"}));
        }
        cbxMODELO.setSelectedIndex(-1);
    }

    public void verificarCAMPOS_RVEHICULOS(vistaVEHICULO vhcl) {
        if (vhcl.txtID_VEHICULO.getText().isEmpty() || vhcl.cbxTIPO.getSelectedIndex() == -1
                || vhcl.cbxMARCA.getSelectedIndex() == -1 || vhcl.cbxMODELO.getSelectedIndex() == -1
                || vhcl.cbxCOLOR.getSelectedIndex() == -1 || vhcl.txtPLACA.getText().isEmpty()
                || (!vhcl.rbtnNO.isSelected() && !vhcl.rbtnSI.isSelected())) {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos");
            return;
        }
        int idVehiculo;
        try {
            idVehiculo = Integer.parseInt(vhcl.txtID_VEHICULO.getText());
            if (idVehiculo < 0) {
                JOptionPane.showMessageDialog(null, "El ID del vehículo debe ser positivo.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID del vehículo debe ser un número.");
            return;
        }

        if (!verificarIdLibre_VEHICULO_RAF(idVehiculo)) {
            JOptionPane.showMessageDialog(null, "El ID de vehículo '" + idVehiculo + "' ya existe.", "ID No Disponible", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tipo = vhcl.cbxTIPO.getSelectedItem().toString();
        String marca = vhcl.cbxMARCA.getSelectedItem().toString();
        String modelo = vhcl.cbxMODELO.getSelectedItem().toString();
        String color = vhcl.cbxCOLOR.getSelectedItem().toString();
        String nroPLACA = vhcl.txtPLACA.getText();
        String lunasPOLARIZADAS = vhcl.rbtnSI.isSelected() ? "SI" : "NO";
        Vehiculo nuevoVehiculo = new Vehiculo(idVehiculo, tipo, marca, modelo, color, nroPLACA, lunasPOLARIZADAS);
        anadirVEHICULO_RAF(nuevoVehiculo);
        //limpiarVEHICULO(vhcl);
    }

    public boolean procesarActualizacionVehiculo(vistaVEHICULO vhcl) {
        if (vhcl.cbxTIPO.getSelectedIndex() == -1 || vhcl.cbxMARCA.getSelectedIndex() == -1
                || vhcl.cbxMODELO.getSelectedIndex() == -1 || vhcl.cbxCOLOR.getSelectedIndex() == -1
                || vhcl.txtPLACA.getText().trim().isEmpty() || (!vhcl.rbtnNO.isSelected() && !vhcl.rbtnSI.isSelected())) {
            JOptionPane.showMessageDialog(vhcl, "Complete todos los campos obligatorios.", "Error Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int idActualizar = Integer.parseInt(vhcl.txtID_VEHICULO.getText());
        String tipo = vhcl.cbxTIPO.getSelectedItem().toString();
        String marca = vhcl.cbxMARCA.getSelectedItem().toString();
        String modelo = vhcl.cbxMODELO.getSelectedItem().toString();
        String color = vhcl.cbxCOLOR.getSelectedItem().toString();
        String nroPLACA = vhcl.txtPLACA.getText().trim();
        String lunasPOLARIZADAS = vhcl.rbtnSI.isSelected() ? "SI" : "NO";
        Vehiculo vehiculoActualizado = new Vehiculo(idActualizar, tipo, marca, modelo, color, nroPLACA, lunasPOLARIZADAS);
        return actualizarVEHICULO_RAF(vehiculoActualizado);
    }

    public void disenoTABLA_VEHICULOS(JTable tablaVEHICULOS) {
        DefaultTableModel modeloTabla_3 = new DefaultTableModel();
        modeloTabla_3.addColumn("idVehiculo");
        modeloTabla_3.addColumn("Tipo");
        modeloTabla_3.addColumn("Marca");
        modeloTabla_3.addColumn("Modelo");
        modeloTabla_3.addColumn("Color");
        modeloTabla_3.addColumn("Nro. Placa");
        modeloTabla_3.addColumn("¿Lunas Polarizadas?");
        leerVEHICULOS_RAF(modeloTabla_3);
        tablaVEHICULOS.setModel(modeloTabla_3);
    }

    public void limpiarVEHICULO(vistaVEHICULO vhcl) {
        vhcl.txtID_VEHICULO.setText("");
        vhcl.cbxTIPO.setSelectedIndex(-1); // Corregido para usar vhcl
        vhcl.cbxMARCA.setSelectedIndex(-1);
        vhcl.cbxMODELO.removeAllItems();
        vhcl.cbxCOLOR.setSelectedIndex(-1);
        vhcl.txtPLACA.setText("");
        vhcl.btnGRP_ASOCIACION.clearSelection();
        vhcl.txtID_VEHICULO.setEditable(true);
        vhcl.btnREGISTRAR.setText("REGISTRAR");
        vhcl.jl_VEHICULO.setText("Registro de Vehiculo");
        vhcl.setTitle("Registro de Vehiculo");
    }

    public void anadirENTRADA_RAF(REGISTRO_ENTRADAS_SALIDAS rge) {
        File archivoDat = new File(ENTRADA_DAT_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            raf.seek(raf.length());
            raf.writeBoolean(true); // activo
            raf.writeInt(rge.getIdEntrada());
            raf.writeInt(rge.getIdEmpleado1());
            raf.writeInt(rge.getIdCliente1());
            raf.writeInt(rge.getIdVehiculo1());
            raf.writeInt(rge.getPiso());
            writeString(raf, rge.getZona(), "-");
            writeString(raf, rge.getFecha_Ingreso(), "00/00/0000");
            writeString(raf, rge.getHora_Ingreso(), "00:00");
            writeString(raf, rge.getTipo_Documento(), "Boleta");
            writeString(raf, rge.getDescripcion(), "-");
            JOptionPane.showMessageDialog(null, "Entrada añadida correctamente (RAF).");
        } catch (IOException ex) {
            System.err.println("Error al añadir entrada (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "No se pudo registrar la entrada (RAF).\n" + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void leerENTRADAS_RAF(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0); // Limpiar tabla antes de cargar
        File archivoDat = new File(ENTRADA_DAT_FILE);
        if (!archivoDat.exists()) {
            System.out.println("Archivo " + ENTRADA_DAT_FILE + " no existe. No hay entradas para mostrar.");
            return;
        }
        System.out.println("DEBUG: Iniciando leerENTRADAS_RAF desde " + ENTRADA_DAT_FILE);
        int contadorRegistrosLeidos = 0;
        int contadorRegistrosActivosAnadidos = 0;

        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            long fileSize = raf.length();
            System.out.println("DEBUG: Tamaño del archivo ENTRADA.dat: " + fileSize + " bytes.");

            while (raf.getFilePointer() < fileSize) {
                long posAntesDeLeerRegistro = raf.getFilePointer();
                // System.out.println("DEBUG: Leyendo registro de entrada en posición: " + posAntesDeLeerRegistro); // Descomentar para depuración detallada

                boolean activo = true;
                int idEntrada = -1, idEmpleado1 = -1, idCliente1 = -1, idVehiculo1 = -1, piso = -1;
                String zona = "ERR", fecha_Ingreso = "ERR", hora_Ingreso = "ERR", tipo_Documento = "ERR", descripcion = "ERR";

                try {
                    activo = raf.readBoolean();
                    idEntrada = raf.readInt();
                    idEmpleado1 = raf.readInt();
                    idCliente1 = raf.readInt();
                    idVehiculo1 = raf.readInt();
                    piso = raf.readInt();
                    zona = readString(raf, "-", false);
                    fecha_Ingreso = readString(raf, "00/00/0000", false);
                    hora_Ingreso = readString(raf, "00:00", false);
                    tipo_Documento = readString(raf, "Boleta", false);
                    descripcion = readString(raf, "-", false);
                    contadorRegistrosLeidos++;

                    // System.out.println("DEBUG: Leído Entrada ID: " + idEntrada + ", Activo: " + activo); // Descomentar para depuración
                    if (activo) {
                        modeloTabla.addRow(new Object[]{
                            idEntrada, idEmpleado1, idCliente1, idVehiculo1, piso,
                            zona, fecha_Ingreso, hora_Ingreso, tipo_Documento, descripcion
                        });
                        contadorRegistrosActivosAnadidos++;
                    }
                } catch (EOFException eof) {
                    System.err.println("ERROR: EOFException inesperada al leer registro de entrada en posición " + posAntesDeLeerRegistro
                            + ". Archivo podría estar corrupto o la lógica de escritura/lectura es incorrecta. Bytes restantes: " + (fileSize - posAntesDeLeerRegistro));
                    eof.printStackTrace(); // Imprimir traza para más detalles
                    break;
                } catch (IOException ioe) {
                    System.err.println("ERROR: IOException al leer campos del registro de entrada que comenzaba en posición " + posAntesDeLeerRegistro + ". ID Parcial (si se leyó): " + idEntrada + ". Mensaje: " + ioe.getMessage());
                    ioe.printStackTrace();
                    break;
                }
            }
            System.out.println("DEBUG: Fin leerENTRADS_RAF. Total registros intentados leer: " + contadorRegistrosLeidos
                    + ". Registros activos añadidos a tabla: " + contadorRegistrosActivosAnadidos);

        } catch (IOException ex) {
            System.err.println("Error al abrir o leer el archivo de entradas (RAF): " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la lista de entradas (RAF).\n" + ex.getMessage(), "Error de Lectura", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean verificarIdLibre_ENTRADA_RAF(int idVerificar) {
        File archivoDat = new File(ENTRADA_DAT_FILE);
        if (!archivoDat.exists()) {
            return true;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                // Saltar campos restantes
                raf.readInt();
                raf.readInt();
                raf.readInt();
                raf.readInt();
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                if (idActual == idVerificar && activo) {
                    return false;
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al verificar ID de entrada (RAF): " + ex.getMessage());
            return false; // Asumir que no está libre en caso de error
        }
        return true;
    }

    public REGISTRO_ENTRADAS_SALIDAS buscarEntradaPorId_RAF(int idBuscar) {
        File archivoDat = new File(ENTRADA_DAT_FILE);
        if (!archivoDat.exists()) {
            return null;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idEntrada = raf.readInt();
                int idEmpleado1 = raf.readInt();
                int idCliente1 = raf.readInt();
                int idVehiculo1 = raf.readInt();
                int piso = raf.readInt();
                String zona = readString(raf, "-", false);
                String fecha_Ingreso = readString(raf, "00/00/0000", false);
                String hora_Ingreso = readString(raf, "00:00", false);
                String tipo_Documento = readString(raf, "Boleta", false);
                String descripcion = readString(raf, "-", false);

                if (idEntrada == idBuscar && activo) {
                    REGISTRO_ENTRADAS_SALIDAS rge = new REGISTRO_ENTRADAS_SALIDAS();
                    rge.setIdEntrada(idEntrada);
                    rge.setIdEmpleado1(idEmpleado1);
                    rge.setIdCliente1(idCliente1);
                    rge.setIdVehiculo1(idVehiculo1);
                    rge.setPiso(piso);
                    rge.setZona(zona);
                    rge.setFecha_Ingreso(fecha_Ingreso);
                    rge.setHora_Ingreso(hora_Ingreso);
                    rge.setTipo_Documento(tipo_Documento);
                    rge.setDescripcion(descripcion);
                    return rge;
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al buscar entrada por ID (RAF): " + ex.getMessage());
        }
        return null;
    }

    public boolean actualizarENTRADA_RAF(REGISTRO_ENTRADAS_SALIDAS rgeActualizado) {
        File archivoDat = new File(ENTRADA_DAT_FILE);
        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo de entradas no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        long posicionRegistro = -1;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long inicioRegistroActual = raf.getFilePointer();
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                if (idActual == rgeActualizado.getIdEntrada() && activo) {
                    posicionRegistro = inicioRegistroActual;
                    break;
                }
                // Saltar campos
                raf.readInt();
                raf.readInt();
                raf.readInt();
                raf.readInt();
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
            }
            if (posicionRegistro != -1) {
                raf.seek(posicionRegistro);
                raf.writeBoolean(true); // Mantener activo
                raf.writeInt(rgeActualizado.getIdEntrada());
                raf.writeInt(rgeActualizado.getIdEmpleado1());
                raf.writeInt(rgeActualizado.getIdCliente1());
                raf.writeInt(rgeActualizado.getIdVehiculo1());
                raf.writeInt(rgeActualizado.getPiso());
                writeString(raf, rgeActualizado.getZona(), "-");
                writeString(raf, rgeActualizado.getFecha_Ingreso(), "00/00/0000");
                writeString(raf, rgeActualizado.getHora_Ingreso(), "00:00");
                writeString(raf, rgeActualizado.getTipo_Documento(), "Boleta");
                writeString(raf, rgeActualizado.getDescripcion(), "-");
                JOptionPane.showMessageDialog(null, "Entrada actualizada (RAF).");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Entrada ID " + rgeActualizado.getIdEntrada() + " no encontrada o inactiva (RAF).", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error al actualizar entrada (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al actualizar entrada (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean eliminarENTRADA_RAF(int idEliminar) {
        File archivoDat = new File(ENTRADA_DAT_FILE);
        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo de entradas no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        long posicionRegistro = -1;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long inicioRegistroActual = raf.getFilePointer();
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                if (idActual == idEliminar && activo) {
                    posicionRegistro = inicioRegistroActual;
                    break;
                }
                // Saltar campos
                raf.readInt();
                raf.readInt();
                raf.readInt();
                raf.readInt();
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
            }
            if (posicionRegistro != -1) {
                raf.seek(posicionRegistro);
                raf.writeBoolean(false); // Marcar como inactivo
                JOptionPane.showMessageDialog(null, "Entrada ID " + idEliminar + " marcada como inactiva (RAF).");
                // Considerar eliminar Salidas asociadas aquí o en un método separado
                eliminarSalidasPorIdEntrada_RAF(idEliminar);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Entrada ID " + idEliminar + " no encontrada o ya inactiva (RAF).", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error al eliminar entrada (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al eliminar entrada (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void verificarCAMPO_ENTRADA(vistaENTRADA veta) {
        if (veta.txtID_ENTRADA.getText().isEmpty() || veta.cbxCLIENTE.getSelectedIndex() == -1
                || veta.cbxEmpleado.getSelectedIndex() == -1 || veta.cbxVehiculo.getSelectedIndex() == -1
                || veta.cbxPiso.getSelectedIndex() == -1 || veta.txtZona.getText().isEmpty()
                || !verificarFECHA(veta.txtFecha) || !verificarHORA(veta.txtHora)
                || veta.cbxDOCUMENTO.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "No deje campos vacios o corrija formatos.");
            return;
        }
        int idEntrada;
        try {
            idEntrada = Integer.parseInt(veta.txtID_ENTRADA.getText());
            if (idEntrada < 0) {
                JOptionPane.showMessageDialog(null, "El ID de registro debe ser positivo.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID de registro debe ser un número.");
            return;
        }

        if (!verificarIdLibre_ENTRADA_RAF(idEntrada)) {
            JOptionPane.showMessageDialog(null, "El ID de registro de entrada '" + idEntrada + "' ya existe.", "ID No Disponible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        REGISTRO_ENTRADAS_SALIDAS rge = new REGISTRO_ENTRADAS_SALIDAS();
        rge.setIdEntrada(idEntrada);
        rge.setIdEmpleado1(Integer.parseInt(veta.cbxEmpleado.getSelectedItem().toString()));
        rge.setIdCliente1(Integer.parseInt(veta.cbxCLIENTE.getSelectedItem().toString()));
        rge.setIdVehiculo1(Integer.parseInt(veta.cbxVehiculo.getSelectedItem().toString()));
        rge.setPiso(Integer.parseInt(veta.cbxPiso.getSelectedItem().toString()));
        rge.setZona(veta.txtZona.getText());
        rge.setFecha_Ingreso(veta.txtFecha.getText());
        rge.setHora_Ingreso(veta.txtHora.getText());
        rge.setTipo_Documento(veta.cbxDOCUMENTO.getSelectedItem().toString());
        rge.setDescripcion(veta.txtDescripcion.getText().isEmpty() ? "-" : veta.txtDescripcion.getText());

        anadirENTRADA_RAF(rge);
        //limpiarENTRADA(veta);
    }

    public boolean procesarActualizacionEntrada(vistaENTRADA veta) {
        if (veta.cbxCLIENTE.getSelectedIndex() == -1 || veta.cbxEmpleado.getSelectedIndex() == -1
                || veta.cbxVehiculo.getSelectedIndex() == -1 || veta.cbxPiso.getSelectedIndex() == -1
                || veta.txtZona.getText().trim().isEmpty() || !verificarFECHA(veta.txtFecha)
                || !verificarHORA(veta.txtHora) || veta.cbxDOCUMENTO.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(veta, "Revise los campos obligatorios y formatos.", "Error Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int idEntradaActualizar = Integer.parseInt(veta.txtID_ENTRADA.getText()); // ID no editable

        // Validación Lógica Fecha/Hora Entrada vs Salida Existente
        String fechaEntradaNuevaStr = veta.txtFecha.getText().trim();
        String horaEntradaNuevaStr = veta.txtHora.getText().trim();

        // Buscar si existe una salida para esta entrada
        REGISTRO_ENTRADAS_SALIDAS salidaExistente = null; // Necesitaríamos un método buscarSalidaPorIdEntrada_RAF
        // Temporalmente, asumimos que no hay validación cruzada o que se hará manualmente
        // if (salidaExistente != null) { ... lógica de validación ... }

        REGISTRO_ENTRADAS_SALIDAS rgeActualizado = new REGISTRO_ENTRADAS_SALIDAS();
        rgeActualizado.setIdEntrada(idEntradaActualizar);
        rgeActualizado.setIdEmpleado1(Integer.parseInt(veta.cbxEmpleado.getSelectedItem().toString()));
        rgeActualizado.setIdCliente1(Integer.parseInt(veta.cbxCLIENTE.getSelectedItem().toString()));
        rgeActualizado.setIdVehiculo1(Integer.parseInt(veta.cbxVehiculo.getSelectedItem().toString()));
        rgeActualizado.setPiso(Integer.parseInt(veta.cbxPiso.getSelectedItem().toString()));
        rgeActualizado.setZona(veta.txtZona.getText());
        rgeActualizado.setFecha_Ingreso(fechaEntradaNuevaStr);
        rgeActualizado.setHora_Ingreso(horaEntradaNuevaStr);
        rgeActualizado.setTipo_Documento(veta.cbxDOCUMENTO.getSelectedItem().toString());
        rgeActualizado.setDescripcion(veta.txtDescripcion.getText().isEmpty() ? "-" : veta.txtDescripcion.getText());

        return actualizarENTRADA_RAF(rgeActualizado);
    }

    public void disenoTABLA_ENTRADAS(JTable tablaENTRADAS) {
        DefaultTableModel modeloTabla_4 = new DefaultTableModel();
        modeloTabla_4.addColumn("idEntrada");
        modeloTabla_4.addColumn("idEmpleado");
        modeloTabla_4.addColumn("idCliente");
        modeloTabla_4.addColumn("idVehiculo");
        modeloTabla_4.addColumn("Piso");
        modeloTabla_4.addColumn("Zona");
        modeloTabla_4.addColumn("Fecha Ing.");
        modeloTabla_4.addColumn("Hora Ing.");
        modeloTabla_4.addColumn("Tipo Doc.");
        modeloTabla_4.addColumn("Descripcion");
        leerENTRADAS_RAF(modeloTabla_4);
        tablaENTRADAS.setModel(modeloTabla_4);
    }

    public void limpiarENTRADA(vistaENTRADA veta) {
        veta.txtID_ENTRADA.setText("");
        veta.cbxCLIENTE.setSelectedIndex(-1);
        veta.cbxEmpleado.setSelectedIndex(-1);
        veta.cbxVehiculo.setSelectedIndex(-1);
        veta.cbxPiso.setSelectedIndex(-1);
        veta.txtZona.setText("");
        veta.txtFecha.setText("");
        veta.txtHora.setText("");
        veta.cbxDOCUMENTO.setSelectedIndex(-1);
        veta.txtDescripcion.setText("");

        veta.txtID_ENTRADA.setEditable(true);
        veta.txtID_ENTRADA.setBackground(Color.WHITE);
        veta.btnREGISTRAR.setText("REGISTRAR");
        veta.jl_ENTRADA.setText("Registro de Entrada");
        veta.setTitle("Registrar Entrada de Vehiculos");
    }

    //----------------------------------------VISTA SALIDA (CON RANDOM ACCESS FILE)----------------------------------------------
    public void anadirSALIDA_RAF(REGISTRO_ENTRADAS_SALIDAS rgs) {
        File archivoDat = new File(SALIDA_DAT_FILE);
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            raf.seek(raf.length());
            raf.writeBoolean(true); // activo
            raf.writeInt(rgs.getIdSalida());
            raf.writeInt(rgs.getIdEntrada2());
            raf.writeInt(rgs.getIdEmpleado2());
            raf.writeInt(rgs.getIdCliente2());
            raf.writeInt(rgs.getIdVehiculo2());
            writeString(raf, rgs.getFecha_Salida(), "00/00/0000");
            writeString(raf, rgs.getHora_Salida(), "00:00");
            writeString(raf, rgs.getDescripcion2(), "-");
            JOptionPane.showMessageDialog(null, "Salida añadida correctamente (RAF).");
        } catch (IOException ex) {
            System.err.println("Error al añadir salida (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "No se pudo registrar la salida (RAF).\n" + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void leerSALIDAS_RAF(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);
        File archivoDat = new File(SALIDA_DAT_FILE);
        if (!archivoDat.exists()) {
            System.out.println("Archivo " + SALIDA_DAT_FILE + " no existe.");
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idSalida = raf.readInt();
                int idEntrada2 = raf.readInt();
                int idEmpleado2 = raf.readInt();
                int idCliente2 = raf.readInt();
                int idVehiculo2 = raf.readInt();
                String fecha_Salida = readString(raf, "00/00/0000", false);
                String hora_Salida = readString(raf, "00:00", false);
                String descripcion2 = readString(raf, "-", false);

                if (activo) {
                    modeloTabla.addRow(new Object[]{
                        idSalida, idEntrada2, idEmpleado2, idCliente2, idVehiculo2,
                        fecha_Salida, hora_Salida, descripcion2
                    });
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al leer salidas (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al cargar lista de salidas (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public REGISTRO_ENTRADAS_SALIDAS buscarSalidaPorId_RAF(int idBuscar) {
        File archivoDat = new File(SALIDA_DAT_FILE);
        if (!archivoDat.exists()) {
            return null;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idSalida = raf.readInt();
                int idEntrada2 = raf.readInt();
                int idEmpleado2 = raf.readInt();
                int idCliente2 = raf.readInt();
                int idVehiculo2 = raf.readInt();
                String fecha_Salida = readString(raf, "00/00/0000", false);
                String hora_Salida = readString(raf, "00:00", false);
                String descripcion2 = readString(raf, "-", false);

                if (idSalida == idBuscar && activo) {
                    REGISTRO_ENTRADAS_SALIDAS rgs = new REGISTRO_ENTRADAS_SALIDAS();
                    rgs.setIdSalida(idSalida);
                    rgs.setIdEntrada2(idEntrada2);
                    rgs.setIdEmpleado2(idEmpleado2);
                    rgs.setIdCliente2(idCliente2);
                    rgs.setIdVehiculo2(idVehiculo2);
                    rgs.setFecha_Salida(fecha_Salida);
                    rgs.setHora_Salida(hora_Salida);
                    rgs.setDescripcion2(descripcion2);
                    return rgs;
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al buscar salida por ID (RAF): " + ex.getMessage());
        }
        return null;
    }

    public boolean verificarIdLibre_SALIDA_RAF(int idVerificar) {
        File archivoDat = new File(SALIDA_DAT_FILE);
        if (!archivoDat.exists()) {
            return true;
        }
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                // Saltar campos
                raf.readInt();
                raf.readInt();
                raf.readInt();
                raf.readInt();
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
                if (idActual == idVerificar && activo) {
                    return false;
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            System.err.println("Error al verificar ID de salida (RAF): " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean actualizarSALIDA_RAF(REGISTRO_ENTRADAS_SALIDAS rgsActualizado) {
        File archivoDat = new File(SALIDA_DAT_FILE);
        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo de salidas no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        long posicionRegistro = -1;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long inicioRegistroActual = raf.getFilePointer();
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                if (idActual == rgsActualizado.getIdSalida() && activo) {
                    posicionRegistro = inicioRegistroActual;
                    break;
                }
                // Saltar campos
                raf.readInt();
                raf.readInt();
                raf.readInt();
                raf.readInt();
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
            }
            if (posicionRegistro != -1) {
                raf.seek(posicionRegistro);
                raf.writeBoolean(true); // Mantener activo
                raf.writeInt(rgsActualizado.getIdSalida());
                raf.writeInt(rgsActualizado.getIdEntrada2());
                raf.writeInt(rgsActualizado.getIdEmpleado2());
                raf.writeInt(rgsActualizado.getIdCliente2());
                raf.writeInt(rgsActualizado.getIdVehiculo2());
                writeString(raf, rgsActualizado.getFecha_Salida(), "00/00/0000");
                writeString(raf, rgsActualizado.getHora_Salida(), "00:00");
                writeString(raf, rgsActualizado.getDescripcion2(), "-");
                JOptionPane.showMessageDialog(null, "Salida actualizada (RAF).");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Salida ID " + rgsActualizado.getIdSalida() + " no encontrada o inactiva (RAF).", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error al actualizar salida (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al actualizar salida (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean eliminarSALIDA_RAF(int idEliminar) {
        File archivoDat = new File(SALIDA_DAT_FILE);
        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "Archivo de salidas no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        long posicionRegistro = -1;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long inicioRegistroActual = raf.getFilePointer();
                boolean activo = raf.readBoolean();
                int idActual = raf.readInt();
                if (idActual == idEliminar && activo) {
                    posicionRegistro = inicioRegistroActual;
                    break;
                }
                // Saltar campos
                raf.readInt();
                raf.readInt();
                raf.readInt();
                raf.readInt();
                readString(raf, "", false);
                readString(raf, "", false);
                readString(raf, "", false);
            }
            if (posicionRegistro != -1) {
                raf.seek(posicionRegistro);
                raf.writeBoolean(false); // Marcar como inactivo
                JOptionPane.showMessageDialog(null, "Salida ID " + idEliminar + " marcada como inactiva (RAF).");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Salida ID " + idEliminar + " no encontrada o ya inactiva (RAF).", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error al eliminar salida (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al eliminar salida (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void eliminarSalidasPorIdEntrada_RAF(int idEntradaAEliminarSalidas) {
        File archivoSalida = new File(SALIDA_DAT_FILE);
        if (!archivoSalida.exists()) {
            System.out.println("INFO: Archivo SALIDA.dat no existe, no hay salidas que eliminar para la entrada " + idEntradaAEliminarSalidas);
            return;
        }
        List<Long> posicionesSalidasAMarcar = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(archivoSalida, "rw")) { // Abrir en rw para marcar
            // Primera pasada: encontrar posiciones de las salidas a marcar como inactivas
            long currentPos = 0;
            while (currentPos < raf.length()) {
                raf.seek(currentPos);
                boolean activo = raf.readBoolean();
                int idSalida = raf.readInt();
                int idEntrada2 = raf.readInt();

                if (activo && idEntrada2 == idEntradaAEliminarSalidas) {
                    posicionesSalidasAMarcar.add(currentPos); // Guardar posición del campo 'activo'
                }
                // Saltar al siguiente registro (estimación, puede ser impreciso si hay muchos UTF largos)
                // Es más seguro leer todos los campos para avanzar correctamente.
                raf.readInt();
                raf.readInt();
                raf.readInt(); // idEmp, idCli, idVeh
                readString(raf, "", false); // fechaS
                readString(raf, "", false); // horaS
                readString(raf, "", false); // descS
                currentPos = raf.getFilePointer();
            }

            // Segunda pasada: marcar las salidas encontradas
            for (long pos : posicionesSalidasAMarcar) {
                raf.seek(pos);
                raf.writeBoolean(false); // Marcar como inactivo
                System.out.println("INFO: Salida asociada a Entrada ID " + idEntradaAEliminarSalidas + " en posición " + pos + " marcada como inactiva.");
            }

            if (!posicionesSalidasAMarcar.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Salidas asociadas a la Entrada ID " + idEntradaAEliminarSalidas + " marcadas como inactivas.");
            }

        } catch (IOException ex) {
            System.err.println("Error al intentar marcar salidas inactivas para Entrada ID " + idEntradaAEliminarSalidas + " (RAF): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al procesar salidas asociadas (RAF).\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void verificarCAMPOS_SALIDA(vistaSALIDA vsld) {
        if (vsld.txtID_SALIDA.getText().isEmpty() || vsld.cbxENTRADA.getSelectedIndex() == -1
                || vsld.txtVEHICULO.getText().isEmpty() || vsld.cbxEmpleado.getSelectedIndex() == -1
                || vsld.txtCLIENTE.getText().isEmpty() || !verificarFECHA(vsld.txtFecha)
                || !verificarHORA(vsld.txtHora)) {
            JOptionPane.showMessageDialog(null, "No deje campos vacios o corrija formatos.");
            return;
        }
        int idSalida;
        try {
            idSalida = Integer.parseInt(vsld.txtID_SALIDA.getText());
            if (idSalida < 0) {
                JOptionPane.showMessageDialog(null, "El ID de Salida debe ser positivo.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID de Salida debe ser un número.");
            return;
        }

        if (!verificarIdLibre_SALIDA_RAF(idSalida)) {
            JOptionPane.showMessageDialog(null, "El ID de Salida '" + idSalida + "' ya existe.", "ID No Disponible", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarFechaHoraSalida(vsld.cbxENTRADA, vsld.txtFecha, vsld.txtHora)) {
            return;
        }

        REGISTRO_ENTRADAS_SALIDAS rgs = new REGISTRO_ENTRADAS_SALIDAS();
        rgs.setIdSalida(idSalida);
        rgs.setIdEntrada2(Integer.parseInt(vsld.cbxENTRADA.getSelectedItem().toString()));
        rgs.setIdEmpleado2(Integer.parseInt(vsld.cbxEmpleado.getSelectedItem().toString()));
        rgs.setIdCliente2(Integer.parseInt(vsld.txtCLIENTE.getText())); // Se obtiene de la entrada
        rgs.setIdVehiculo2(Integer.parseInt(vsld.txtVEHICULO.getText())); // Se obtiene de la entrada
        rgs.setFecha_Salida(vsld.txtFecha.getText());
        rgs.setHora_Salida(vsld.txtHora.getText());
        rgs.setDescripcion2(vsld.txtDescripcion.getText().isEmpty() ? "-" : vsld.txtDescripcion.getText());

        anadirSALIDA_RAF(rgs);
        limpiarCAMPOS_SALIDA(vsld);
        // Volver a cargar el combo de entradas para que la entrada recién "salida" ya no aparezca
        cargarCOMBO_ENTRADA(vsld.cbxENTRADA, vsld.btnREGISTRAR);
    }

    public boolean procesarActualizacionSalida(vistaSALIDA vsld) {
        if (vsld.cbxENTRADA.getSelectedIndex() == -1 || vsld.cbxEmpleado.getSelectedIndex() == -1
                || !verificarFECHA(vsld.txtFecha) || !verificarHORA(vsld.txtHora)) {
            JOptionPane.showMessageDialog(vsld, "Revise campos obligatorios y formatos.", "Error Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int idSalidaActualizar = Integer.parseInt(vsld.txtID_SALIDA.getText()); // ID no editable

        if (!validarFechaHoraSalida(vsld.cbxENTRADA, vsld.txtFecha, vsld.txtHora)) {
            return false;
        }

        REGISTRO_ENTRADAS_SALIDAS rgsActualizado = new REGISTRO_ENTRADAS_SALIDAS();
        rgsActualizado.setIdSalida(idSalidaActualizar);
        rgsActualizado.setIdEntrada2(Integer.parseInt(vsld.cbxENTRADA.getSelectedItem().toString()));
        rgsActualizado.setIdEmpleado2(Integer.parseInt(vsld.cbxEmpleado.getSelectedItem().toString()));
        rgsActualizado.setIdCliente2(Integer.parseInt(vsld.txtCLIENTE.getText()));
        rgsActualizado.setIdVehiculo2(Integer.parseInt(vsld.txtVEHICULO.getText()));
        rgsActualizado.setFecha_Salida(vsld.txtFecha.getText());
        rgsActualizado.setHora_Salida(vsld.txtHora.getText());
        rgsActualizado.setDescripcion2(vsld.txtDescripcion.getText().isEmpty() ? "-" : vsld.txtDescripcion.getText());

        return actualizarSALIDA_RAF(rgsActualizado);
    }

    public void disenoTABLA_SALIDAS(JTable tablaSALIDAS) {
        DefaultTableModel modeloTabla_5 = new DefaultTableModel();
        modeloTabla_5.addColumn("idSalida");
        modeloTabla_5.addColumn("idEntrada");
        modeloTabla_5.addColumn("idEmpleado");
        modeloTabla_5.addColumn("idCliente");
        modeloTabla_5.addColumn("idVehiculo");
        modeloTabla_5.addColumn("Fecha Sal.");
        modeloTabla_5.addColumn("Hora Sal.");
        modeloTabla_5.addColumn("Descripcion");
        leerSALIDAS_RAF(modeloTabla_5);
        tablaSALIDAS.setModel(modeloTabla_5);
    }

    public void limpiarCAMPOS_SALIDA(vistaSALIDA vsld) {
        vsld.txtID_SALIDA.setText("");
        vsld.cbxENTRADA.setSelectedIndex(-1);
        vsld.txtVEHICULO.setText("");
        vsld.cbxEmpleado.setSelectedIndex(-1);
        vsld.txtCLIENTE.setText("");
        vsld.txtFecha.setText("");
        vsld.txtHora.setText("");
        vsld.txtDescripcion.setText("");

        vsld.txtID_SALIDA.setEditable(true); // O false si el ID es autogenerado o igual al de entrada
        vsld.txtID_SALIDA.setBackground(Color.WHITE); // O amarillo si es no editable y autocompletado
        vsld.btnREGISTRAR.setText("REGISTRAR");
        vsld.jl_SALIDA.setText("Registro de Salida");
        vsld.setTitle("Registrar Salida de Vehiculos");
        vsld.cbxENTRADA.setEnabled(true); // Asegurar que esté habilitado para nueva selección
    }

    // --- Métodos Comunes y de Carga de Combos ---
    public void cargarCOMBO_CLIENTE(JComboBox cbxCLIENTE) {
        cbxCLIENTE.removeAllItems();
        File archivoDat = new File(CLIENTE_DAT_FILE);
        if (!archivoDat.exists()) {
            System.out.println("Archivo " + CLIENTE_DAT_FILE + " no existe para cargar combo clientes.");
            cbxCLIENTE.setEnabled(false);
            return;
        }
        cbxCLIENTE.setEnabled(true);
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idCliente = raf.readInt();
                // Saltar campos
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "Sin Nombre", false);
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                if (activo) {
                    cbxCLIENTE.addItem(String.valueOf(idCliente));
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar combo clientes (RAF).");
        }
        cbxCLIENTE.setSelectedIndex(-1);
    }

    public void cargarCOMBO_EMPLEADO(JComboBox cbxEMPLEADO) {
        cbxEMPLEADO.removeAllItems();
        File archivoDat = new File(EMPLEADO_DAT_FILE);
        if (!archivoDat.exists()) {
            System.out.println("Archivo " + EMPLEADO_DAT_FILE + " no existe para cargar combo empleados.");
            cbxEMPLEADO.setEnabled(false);
            return;
        }
        cbxEMPLEADO.setEnabled(true);
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int idEmpleado = raf.readInt();
                // Saltar campos
                raf.readInt();
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "Sin Nombre", false);
                raf.readInt();
                readString(raf, "00/00/0000", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                readString(raf, "-", false);
                if (activo) {
                    cbxEMPLEADO.addItem(String.valueOf(idEmpleado));
                }
            }
        } catch (EOFException e) {
            /* Fin */ } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar combo empleados (RAF).");
        }
        cbxEMPLEADO.setSelectedIndex(-1);
    }

    public void cargarCOMBO_VEHICULO(JComboBox<String> cbxVehiculo, boolean filtrarVehiculosYaEstacionados) {
        System.out.println("DEBUG: Iniciando cargarCOMBO_VEHICULO (Filtrando=" + filtrarVehiculosYaEstacionados + ")...");
        cbxVehiculo.removeAllItems();
        cbxVehiculo.setEnabled(true);

        File archivoVehiculo = new File(VEHICULO_DAT_FILE);
        File archivoEntrada = new File(ENTRADA_DAT_FILE);
        File archivoSalida = new File(SALIDA_DAT_FILE);

        if (!archivoVehiculo.exists()) {
            System.err.println("Archivo " + VEHICULO_DAT_FILE + " no existe. No se pueden cargar vehículos.");
            JOptionPane.showMessageDialog(null, "Archivo de Vehículos no encontrado. No se pueden cargar IDs.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            cbxVehiculo.setEnabled(false);
            return;
        }

        Set<Integer> idsVehiculosConEntradaActivaSinSalida = new HashSet<>();

        if (filtrarVehiculosYaEstacionados) {
            Set<Integer> idsEntradaConSalidaActiva = new HashSet<>();
            if (archivoSalida.exists()) {
                try (RandomAccessFile rafSalida = new RandomAccessFile(archivoSalida, "r")) {
                    System.out.println("DEBUG cargarCOMBO_VEHICULO: Leyendo SALIDA.dat para filtrar...");
                    while (rafSalida.getFilePointer() < rafSalida.length()) {
                        long posAntesSalida = rafSalida.getFilePointer();
                        try {
                            boolean activoSalida = rafSalida.readBoolean();
                            rafSalida.readInt(); // idSalida
                            int idEntradaEnSalida = rafSalida.readInt();
                            rafSalida.readInt();
                            rafSalida.readInt();
                            rafSalida.readInt();
                            readString(rafSalida, "", false);
                            readString(rafSalida, "", false);
                            readString(rafSalida, "", false);
                            if (activoSalida) {
                                idsEntradaConSalidaActiva.add(idEntradaEnSalida);
                            }
                        } catch (EOFException eof) {
                            System.err.println("EOFException en cargarCOMBO_VEHICULO leyendo SALIDA.dat en pos " + posAntesSalida + ". Fin de archivo.");
                            break;
                        } catch (IOException ioe) {
                            System.err.println("IOException en cargarCOMBO_VEHICULO leyendo SALIDA.dat en pos " + posAntesSalida + ": " + ioe.getMessage());
                            ioe.printStackTrace(); // Muy importante para ver el error "null"
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error abriendo SALIDA.dat para filtrar vehículos: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("DEBUG cargarCOMBO_VEHICULO: Archivo SALIDA.dat no existe, no se filtran por salidas activas.");
            }
            System.out.println("DEBUG: IDs de Entradas que ya tienen Salida activa: " + idsEntradaConSalidaActiva);

            if (archivoEntrada.exists()) {
                try (RandomAccessFile rafEntrada = new RandomAccessFile(archivoEntrada, "r")) {
                    System.out.println("DEBUG cargarCOMBO_VEHICULO: Leyendo ENTRADA.dat para identificar vehículos estacionados...");
                    while (rafEntrada.getFilePointer() < rafEntrada.length()) {
                        long posAntesEntrada = rafEntrada.getFilePointer();
                        try {
                            boolean activoEntrada = rafEntrada.readBoolean();
                            int idEntrada = rafEntrada.readInt();
                            rafEntrada.readInt(); // idEmpleado1
                            rafEntrada.readInt(); // idCliente1
                            int idVehiculoEnEntrada = rafEntrada.readInt();
                            rafEntrada.readInt(); // piso
                            readString(rafEntrada, "", false);
                            readString(rafEntrada, "", false);
                            readString(rafEntrada, "", false);
                            readString(rafEntrada, "", false);
                            readString(rafEntrada, "", false);

                            if (activoEntrada && !idsEntradaConSalidaActiva.contains(idEntrada)) {
                                idsVehiculosConEntradaActivaSinSalida.add(idVehiculoEnEntrada);
                            }
                        } catch (EOFException eof) {
                            System.err.println("EOFException en cargarCOMBO_VEHICULO leyendo ENTRADA.dat en pos " + posAntesEntrada + ". Fin de archivo.");
                            break;
                        } catch (IOException ioe) {
                            System.err.println("ERROR CRITICO: IOException en cargarCOMBO_VEHICULO leyendo ENTRADA.dat en pos " + posAntesEntrada + ": " + ioe.getMessage());
                            ioe.printStackTrace(); // ESTA ES LA TRAZA QUE NECESITAMOS VER SI EL ERROR ES "null"
                            JOptionPane.showMessageDialog(null, "Error crítico leyendo ENTRADA.dat al filtrar vehículos: " + ioe.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error abriendo ENTRADA.dat para filtrar vehículos: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("DEBUG cargarCOMBO_VEHICULO: Archivo ENTRADA.dat no existe, no se pueden determinar vehículos estacionados.");
            }
            System.out.println("DEBUG: IDs de Vehículos actualmente DENTRO (activos y sin salida activa, a excluir si filtrar=true): " + idsVehiculosConEntradaActivaSinSalida);
        }

        boolean algunVehiculoCargado = false;
        try (RandomAccessFile rafVehiculo = new RandomAccessFile(archivoVehiculo, "r")) {
            while (rafVehiculo.getFilePointer() < rafVehiculo.length()) {
                long posAntesVehiculo = rafVehiculo.getFilePointer();
                try {
                    boolean activoVehiculo = rafVehiculo.readBoolean();
                    int idVehiculo = rafVehiculo.readInt();
                    readString(rafVehiculo, "", false);
                    readString(rafVehiculo, "", false);
                    readString(rafVehiculo, "", false);
                    readString(rafVehiculo, "", false);
                    readString(rafVehiculo, "", false);
                    readString(rafVehiculo, "", false);

                    if (activoVehiculo) {
                        if (filtrarVehiculosYaEstacionados) {
                            if (!idsVehiculosConEntradaActivaSinSalida.contains(idVehiculo)) {
                                cbxVehiculo.addItem(String.valueOf(idVehiculo));
                                algunVehiculoCargado = true;
                            }
                        } else {
                            cbxVehiculo.addItem(String.valueOf(idVehiculo));
                            algunVehiculoCargado = true;
                        }
                    }
                } catch (EOFException eof) {
                    System.err.println("EOFException en cargarCOMBO_VEHICULO leyendo VEHICULO.dat en pos " + posAntesVehiculo + ". Fin de archivo.");
                    break;
                } catch (IOException ioe) {
                    System.err.println("IOException en cargarCOMBO_VEHICULO leyendo VEHICULO.dat en pos " + posAntesVehiculo + ": " + ioe.getMessage());
                    ioe.printStackTrace();
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar combo vehículos (RAF).\n" + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            cbxVehiculo.setEnabled(false);
            return;
        }

        if (!algunVehiculoCargado) {
            String mensaje = filtrarVehiculosYaEstacionados
                    ? "No hay Vehículos disponibles (o todos ya están dentro)."
                    : "No hay vehículos activos registrados en el sistema.";
            JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
            cbxVehiculo.setEnabled(false);
        } else {
            cbxVehiculo.setSelectedIndex(-1);
        }
        System.out.println("DEBUG: Finalizando cargarCOMBO_VEHICULO (Filtrando=" + filtrarVehiculosYaEstacionados + "). Items: " + cbxVehiculo.getItemCount());
    }

    public void cargarCOMBO_ENTRADA(JComboBox cbxENTRADA, JButton btnREGISTRAR) {
        cbxENTRADA.removeAllItems();
        File archivoEntrada = new File(ENTRADA_DAT_FILE);
        File archivoSalida = new File(SALIDA_DAT_FILE);
        Set<Integer> idsEntradaConSalidaActiva = new HashSet<>();

        // 1. Obtener IDs de entradas que ya tienen una salida activa
        if (archivoSalida.exists()) {
            try (RandomAccessFile rafSalida = new RandomAccessFile(archivoSalida, "r")) {
                while (rafSalida.getFilePointer() < rafSalida.length()) {
                    boolean activoSalida = rafSalida.readBoolean();
                    rafSalida.readInt(); // idSalida
                    int idEntradaEnSalida = rafSalida.readInt();
                    // Saltar resto de campos de salida
                    rafSalida.readInt();
                    rafSalida.readInt();
                    rafSalida.readInt();
                    readString(rafSalida, "", false);
                    readString(rafSalida, "", false);
                    readString(rafSalida, "", false);
                    if (activoSalida) {
                        idsEntradaConSalidaActiva.add(idEntradaEnSalida);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error leyendo SALIDA.dat para combo entrada: " + e.getMessage());
            }
        }

        // 2. Cargar IDs de entrada que están activos y NO tienen salida activa
        boolean algunaEntradaCargada = false;
        if (archivoEntrada.exists()) {
            try (RandomAccessFile rafEntrada = new RandomAccessFile(archivoEntrada, "r")) {
                while (rafEntrada.getFilePointer() < rafEntrada.length()) {
                    boolean activoEntrada = rafEntrada.readBoolean();
                    int idEntrada = rafEntrada.readInt();
                    // Saltar resto de campos de entrada
                    rafEntrada.readInt();
                    rafEntrada.readInt();
                    rafEntrada.readInt();
                    rafEntrada.readInt();
                    readString(rafEntrada, "", false);
                    readString(rafEntrada, "", false);
                    readString(rafEntrada, "", false);
                    readString(rafEntrada, "", false);
                    readString(rafEntrada, "", false);

                    if (activoEntrada && !idsEntradaConSalidaActiva.contains(idEntrada)) {
                        cbxENTRADA.addItem(String.valueOf(idEntrada));
                        algunaEntradaCargada = true;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error leyendo ENTRADA.dat para combo entrada: " + e.getMessage());
            }
        }

        if (!algunaEntradaCargada) {
            JOptionPane.showMessageDialog(null, "No hay entradas pendientes de registrar salida.", "Información", JOptionPane.INFORMATION_MESSAGE);
            cbxENTRADA.setEnabled(false);
            if (btnREGISTRAR != null) {
                btnREGISTRAR.setEnabled(false);
            }
        } else {
            cbxENTRADA.setEnabled(true);
            if (btnREGISTRAR != null) {
                btnREGISTRAR.setEnabled(true);
            }
            cbxENTRADA.setSelectedIndex(-1);
        }
    }

    public void cargar_idVehiculo(JComboBox cbxENTRADA, JTextField txtVEHICULO) {
        if (cbxENTRADA.getSelectedIndex() == -1 || cbxENTRADA.getSelectedItem() == null) {
            txtVEHICULO.setText("");
            return;
        }
        int idEntradaSeleccionada = Integer.parseInt(cbxENTRADA.getSelectedItem().toString());
        REGISTRO_ENTRADAS_SALIDAS entrada = buscarEntradaPorId_RAF(idEntradaSeleccionada);
        if (entrada != null) {
            txtVEHICULO.setText(String.valueOf(entrada.getIdVehiculo1()));
        } else {
            txtVEHICULO.setText("N/A"); // O vacío
        }
    }

    public void cargar_idCliente(JComboBox cbxENTRADA, JTextField txtCLIENTE) {
        if (cbxENTRADA.getSelectedIndex() == -1 || cbxENTRADA.getSelectedItem() == null) {
            txtCLIENTE.setText("");
            return;
        }
        int idEntradaSeleccionada = Integer.parseInt(cbxENTRADA.getSelectedItem().toString());
        REGISTRO_ENTRADAS_SALIDAS entrada = buscarEntradaPorId_RAF(idEntradaSeleccionada);
        if (entrada != null) {
            txtCLIENTE.setText(String.valueOf(entrada.getIdCliente1()));
        } else {
            txtCLIENTE.setText("N/A");
        }
    }

    public void cargar_idEMPLEADO(JComboBox cbxENTRADA, JComboBox cbxEMPLEADO) {
        if (cbxENTRADA.getSelectedIndex() == -1 || cbxENTRADA.getSelectedItem() == null) {
            cbxEMPLEADO.setSelectedIndex(-1);
            return;
        }
        int idEntradaSeleccionada = Integer.parseInt(cbxENTRADA.getSelectedItem().toString());
        REGISTRO_ENTRADAS_SALIDAS entrada = buscarEntradaPorId_RAF(idEntradaSeleccionada);
        if (entrada != null) {
            cbxEMPLEADO.setSelectedItem(String.valueOf(entrada.getIdEmpleado1()));
            if (cbxEMPLEADO.getSelectedIndex() == -1) { // Si el empleado no está en el combo (p.ej. inactivo)
                System.err.println("Advertencia: Empleado ID " + entrada.getIdEmpleado1() + " de la entrada no encontrado en el combo.");
            }
        } else {
            cbxEMPLEADO.setSelectedIndex(-1);
        }
    }

    // --- Métodos de Validación (Fecha, Hora, Edad) ---
    public boolean verificarFECHA(JTextField txtFECHA) {
        try {
            if (txtFECHA.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese una fecha");
                return false;
            }
            if (txtFECHA.getText().length() != 10 || !txtFECHA.getText().matches("\\d{2}/\\d{2}/\\d{4}")) {
                JOptionPane.showMessageDialog(null, "Formato incorrecto. Use dd/mm/aaaa");
                return false;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.parse(txtFECHA.getText());
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fecha inválida");
            return false;
        }
    }

    public boolean verificarHORA(JTextField txtHORA) {
        try {
            if (txtHORA.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese una hora");
                return false;
            }
            if (txtHORA.getText().length() != 5 || !txtHORA.getText().matches("\\d{2}:\\d{2}")) {
                JOptionPane.showMessageDialog(null, "Formato incorrecto. Use hh:mm");
                return false;
            }
            String[] partes = txtHORA.getText().split(":");
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);
            if (horas < 0 || horas > 23 || minutos < 0 || minutos > 59) {
                JOptionPane.showMessageDialog(null, "Hora fuera de rango.");
                return false;
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hora inválida");
            return false;
        }
    }

    private boolean esMayorDeEdad(String fechaNacimientoStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formatter);
            LocalDate ahora = LocalDate.now();
            return Period.between(fechaNacimiento, ahora).getYears() >= 18;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validarFechaHoraSalida(JComboBox cbxENTRADA, JTextField txtFechaSalida, JTextField txtHoraSalida) {
        if (cbxENTRADA.getSelectedIndex() == -1 || cbxENTRADA.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Seleccione una Entrada válida primero.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int idEntrada = Integer.parseInt(cbxENTRADA.getSelectedItem().toString());
        REGISTRO_ENTRADAS_SALIDAS entrada = buscarEntradaPorId_RAF(idEntrada);

        if (entrada == null) {
            JOptionPane.showMessageDialog(null, "No se encontró el registro de entrada asociado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String fechaEntradaStr = entrada.getFecha_Ingreso();
        String horaEntradaStr = entrada.getHora_Ingreso();
        String fechaSalidaStr = txtFechaSalida.getText().trim();
        String horaSalidaStr = txtHoraSalida.getText().trim();

        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalDate fechaEntrada = LocalDate.parse(fechaEntradaStr, dateFormatter);
            LocalTime horaEntrada = LocalTime.parse(horaEntradaStr, timeFormatter);
            LocalDate fechaSalida = LocalDate.parse(fechaSalidaStr, dateFormatter);
            LocalTime horaSalida = LocalTime.parse(horaSalidaStr, timeFormatter);

            if (fechaSalida.isBefore(fechaEntrada)) {
                JOptionPane.showMessageDialog(null, "La Fecha de Salida no puede ser anterior a la Fecha de Entrada.", "Error de Fecha", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (fechaSalida.isEqual(fechaEntrada) && !horaSalida.isAfter(horaEntrada)) {
                JOptionPane.showMessageDialog(null, "En la misma fecha, la Hora de Salida debe ser posterior a la Hora de Entrada.", "Error de Hora", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Error al parsear fechas/horas para validación: " + e.getMessage(), "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // --- Métodos de Eliminación, Búsqueda y Actualización (General) ---
    public void cargarIdsParaEliminar(JComboBox cbxTIPO, JComboBox cbxID) {
        cbxID.removeAllItems();
        String tipoSeleccionado = cbxTIPO.getSelectedItem().toString().toUpperCase();
        String archivoDatNombre = BASE_DATA_PATH + tipoSeleccionado + ".dat";
        File archivoDat = new File(archivoDatNombre);

        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "No existen registros de tipo '" + tipoSeleccionado + "' (RAF).", "Archivo No Encontrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean cargadoAlgunID = false;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int id = raf.readInt();
                // Saltar el resto del registro específico del tipo
                if (tipoSeleccionado.equals("EMPLEADO")) {
                    raf.readInt();
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "Sin Nombre", false);
                    raf.readInt();
                    readString(raf, "00/00/0000", false);
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                } else if (tipoSeleccionado.equals("CLIENTE")) {
                    raf.readInt();
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "Sin Nombre", false);
                    raf.readInt();
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                    readString(raf, "-", false);
                } else if (tipoSeleccionado.equals("VEHICULO")) {
                    readString(raf, "", false);
                    readString(raf, "", false);
                    readString(raf, "", false);
                    readString(raf, "", false);
                    readString(raf, "", false);
                    readString(raf, "", false);
                } else if (tipoSeleccionado.equals("ENTRADA")) {
                    raf.readInt();
                    raf.readInt();
                    raf.readInt();
                    raf.readInt();
                    readString(raf, "", false);
                    readString(raf, "", false);
                    readString(raf, "", false);
                    readString(raf, "", false);
                    readString(raf, "", false);
                } else if (tipoSeleccionado.equals("SALIDA")) {
                    raf.readInt();
                    raf.readInt();
                    raf.readInt();
                    raf.readInt();
                    readString(raf, "", false);
                    readString(raf, "", false);
                    readString(raf, "", false);
                }

                if (activo) {
                    cbxID.addItem(String.valueOf(id));
                    cargadoAlgunID = true;
                }
            }
            if (!cargadoAlgunID) {
                JOptionPane.showMessageDialog(null, "No hay " + tipoSeleccionado + "s activos para eliminar (RAF).");
            } else {
                cbxID.setSelectedIndex(-1);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar IDs de " + tipoSeleccionado + " (RAF).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean solicitarConfirmacionYEliminar(JComboBox cbxTIPO, JComboBox cbxID_A_Eliminar) {
        if (cbxTIPO.getSelectedIndex() == -1 || cbxID_A_Eliminar.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione TIPO e ID a eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String tipo = cbxTIPO.getSelectedItem().toString();
        String idStr = cbxID_A_Eliminar.getSelectedItem().toString();
        int id = Integer.parseInt(idStr);

        String mensajeConfirmacion = "¿Está seguro de eliminar el " + tipo + " con ID " + id + "?\n(Esto lo marcará como inactivo)";
        if (tipo.equalsIgnoreCase("ENTRADA")) {
            mensajeConfirmacion += "\n¡Esto también marcará como inactivas las Salidas asociadas!";
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                mensajeConfirmacion,
                "Confirmar Eliminación (Lógica)", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = false;
            if (tipo.equalsIgnoreCase("EMPLEADO")) {
                eliminado = eliminarEMPLEADO_RAF(id);
            } else if (tipo.equalsIgnoreCase("CLIENTE")) {
                eliminado = eliminarCLIENTE_RAF(id);
            } else if (tipo.equalsIgnoreCase("VEHICULO")) {
                eliminado = eliminarVEHICULO_RAF(id);
            } else if (tipo.equalsIgnoreCase("ENTRADA")) {
                eliminado = eliminarENTRADA_RAF(id);
            } else if (tipo.equalsIgnoreCase("SALIDA")) {
                eliminado = eliminarSALIDA_RAF(id);
            } else {
                JOptionPane.showMessageDialog(null, "Eliminación para tipo '" + tipo + "' con RAF pendiente.");
                return false;
            }
            if (eliminado) {
                cargarIdsParaEliminar(cbxTIPO, cbxID_A_Eliminar);
            }
            return eliminado;
        }
        return false;
    }

    public void cargarDATOS_BUSQUEDA(JComboBox cbxTIPO, JComboBox cbxID_A_BUSCAR, JTable tablaResultados, JLabel txtESCRIBIR, vistaRESULTADOS vrts) {
        if (cbxTIPO.getSelectedIndex() == -1 || cbxID_A_BUSCAR.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione TIPO e ID para buscar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tipo = cbxTIPO.getSelectedItem().toString();
        String idStr = cbxID_A_BUSCAR.getSelectedItem().toString();
        int id = Integer.parseInt(idStr);
        txtESCRIBIR.setText("Resultados para: " + tipo + " ID " + id);

        DefaultTableModel modelo = new DefaultTableModel();
        tablaResultados.setModel(modelo);

        if (tipo.equalsIgnoreCase("EMPLEADO")) {
            modelo.addColumn("ID");
            modelo.addColumn("Nac.");
            modelo.addColumn("DNI");
            modelo.addColumn("Carnet");
            modelo.addColumn("Pasaporte");
            modelo.addColumn("Resid.");
            modelo.addColumn("Nombre");
            modelo.addColumn("Sexo");
            modelo.addColumn("Fec.Nac");
            modelo.addColumn("Telf");
            modelo.addColumn("Correo");
            modelo.addColumn("Direcc");
            modelo.addColumn("Distrito");
            Empleado emp = buscarEmpleadoPorId_RAF(id);
            if (emp != null) {
                modelo.addRow(new Object[]{
                    emp.getIdEmpleado(), emp.getNacionalidad(), emp.getDni(), emp.getCarnetExtranjeria(), emp.getPasaporte(),
                    emp.getResidencia(), emp.getN_A(), emp.getSexo(), emp.getFecha_nacimiento(), emp.getTelefono(),
                    emp.getCorreo(), emp.getDireccion(), emp.getDistrito()
                });
                vrts.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Empleado con ID " + id + " no encontrado o inactivo (RAF).");
            }
        } else if (tipo.equalsIgnoreCase("CLIENTE")) {
            modelo.addColumn("ID");
            modelo.addColumn("Nac.");
            modelo.addColumn("DNI");
            modelo.addColumn("Carnet");
            modelo.addColumn("Pasaporte");
            modelo.addColumn("Resid.");
            modelo.addColumn("Nombre");
            modelo.addColumn("Sexo");
            modelo.addColumn("Licencia");
            modelo.addColumn("Telf");
            modelo.addColumn("Correo");
            modelo.addColumn("Direcc");
            modelo.addColumn("Distrito");
            Cliente clt = buscarClientePorId_RAF(id);
            if (clt != null) {
                modelo.addRow(new Object[]{
                    clt.getIdCliente(), clt.getNacionalidad(), clt.getDni(), clt.getCarnetExtranjeria(), clt.getPasaporte(),
                    clt.getResidencia(), clt.getN_A(), clt.getSexo(), clt.getNro_licencia(), clt.getTelefono(),
                    clt.getCorreo(), clt.getDireccion(), clt.getDistrito()
                });
                vrts.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente con ID " + id + " no encontrado o inactivo (RAF).");
            }
        } else if (tipo.equalsIgnoreCase("VEHICULO")) {
            modelo.addColumn("ID");
            modelo.addColumn("Tipo");
            modelo.addColumn("Marca");
            modelo.addColumn("Modelo");
            modelo.addColumn("Color");
            modelo.addColumn("Placa");
            modelo.addColumn("Lunas Polarizadas");
            Vehiculo vhcl = buscarVehiculoPorId_RAF(id);
            if (vhcl != null) {
                modelo.addRow(new Object[]{
                    vhcl.getIdVehiculo(), vhcl.getTipo(), vhcl.getIdMARCA(), vhcl.getModelo(),
                    vhcl.getColor(), vhcl.getNroPLACA(), vhcl.getLunasPOLARIZADAS()
                });
                vrts.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Vehículo con ID " + id + " no encontrado o inactivo (RAF).");
            }
        } else if (tipo.equalsIgnoreCase("ENTRADA")) {
            modelo.addColumn("idEntrada");
            modelo.addColumn("idEmpleado");
            modelo.addColumn("idCliente");
            modelo.addColumn("idVehiculo");
            modelo.addColumn("Piso");
            modelo.addColumn("Zona");
            modelo.addColumn("Fecha Ing.");
            modelo.addColumn("Hora Ing.");
            modelo.addColumn("Tipo Doc.");
            modelo.addColumn("Descripcion");
            REGISTRO_ENTRADAS_SALIDAS entrada = buscarEntradaPorId_RAF(id);
            if (entrada != null) {
                modelo.addRow(new Object[]{
                    entrada.getIdEntrada(), entrada.getIdEmpleado1(), entrada.getIdCliente1(), entrada.getIdVehiculo1(),
                    entrada.getPiso(), entrada.getZona(), entrada.getFecha_Ingreso(), entrada.getHora_Ingreso(),
                    entrada.getTipo_Documento(), entrada.getDescripcion()
                });
                vrts.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Entrada con ID " + id + " no encontrada o inactiva (RAF).");
            }
        } else if (tipo.equalsIgnoreCase("SALIDA")) {
            modelo.addColumn("idSalida");
            modelo.addColumn("idEntrada");
            modelo.addColumn("idEmpleado");
            modelo.addColumn("idCliente");
            modelo.addColumn("idVehiculo");
            modelo.addColumn("Fecha Sal.");
            modelo.addColumn("Hora Sal.");
            modelo.addColumn("Descripcion");
            REGISTRO_ENTRADAS_SALIDAS salida = buscarSalidaPorId_RAF(id);
            if (salida != null) {
                modelo.addRow(new Object[]{
                    salida.getIdSalida(), salida.getIdEntrada2(), salida.getIdEmpleado2(), salida.getIdCliente2(),
                    salida.getIdVehiculo2(), salida.getFecha_Salida(), salida.getHora_Salida(), salida.getDescripcion2()
                });
                vrts.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Salida con ID " + id + " no encontrada o inactiva (RAF).");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Búsqueda para tipo '" + tipo + "' con RAF pendiente.");
        }
    }

    public void actualizar(vistaEMPLEADO vemp, vistaCLIENTE vclt, vistaVEHICULO vhcl, vistaENTRADA vopen, vistaSALIDA vexit,
            JComboBox cbxTIPO, JComboBox cbxID) {
        if (cbxTIPO.getSelectedIndex() == -1 || cbxID.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione TIPO e ID para actualizar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tipo = cbxTIPO.getSelectedItem().toString().toUpperCase();
        String idStr = cbxID.getSelectedItem().toString();
        int id = Integer.parseInt(idStr);

        switch (tipo) {
            // ... (casos para EMPLEADO, CLIENTE, VEHICULO se mantienen igual) ...
            case "EMPLEADO":
                Empleado emp = buscarEmpleadoPorId_RAF(id);
                if (emp != null) {
                    vemp.txtID_EMPLEADO.setText(String.valueOf(emp.getIdEmpleado()));
                    vemp.cbxNACIONALIDAD.setSelectedIndex(emp.getNacionalidad());
                    vemp.txtDNI.setText(emp.getDni() != null ? emp.getDni() : "");
                    vemp.txtEXTRANJERIA.setText(emp.getCarnetExtranjeria() != null ? emp.getCarnetExtranjeria() : "");
                    vemp.txtPASAPORTE.setText(emp.getPasaporte() != null ? emp.getPasaporte() : "");
                    if ("Si".equalsIgnoreCase(emp.getResidencia())) {
                        vemp.rbtnSI.setSelected(true);
                    } else if ("No".equalsIgnoreCase(emp.getResidencia())) {
                        vemp.rbtnNO.setSelected(true);
                    } else {
                        vemp.btnGRP_ASOCIACION.clearSelection();
                    }
                    vemp.txtN_A.setText(emp.getN_A());
                    vemp.cbxSEXO.setSelectedIndex(emp.getSexo());
                    vemp.txtFECHA.setText(emp.getFecha_nacimiento());
                    vemp.txtTELEFONO.setText(emp.getTelefono());
                    vemp.txtCORREO.setText(emp.getCorreo());
                    vemp.txtDIRECCION.setText(emp.getDireccion());
                    vemp.txtDISTRITO.setText(emp.getDistrito());
                    verificarEMPLEADOS(vemp);
                    radioBOTONES(vemp);
                    vemp.txtID_EMPLEADO.setEditable(false);
                    vemp.txtID_EMPLEADO.setBackground(Color.YELLOW);
                    vemp.btnREGISTRAR.setText("ACTUALIZAR");
                    vemp.jl_EMPLEADO.setText("Actualización de Empleado");
                    vemp.setTitle("Actualización de Empleado ID: " + id);
                    vemp.btnLIMPIAR.setVisible(false);
                    vemp.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Empleado con ID " + id + " no encontrado o inactivo (RAF).");
                }
                break;
            case "CLIENTE":
                Cliente clt = buscarClientePorId_RAF(id);
                if (clt != null) {
                    vclt.txtID_CLIENTE.setText(String.valueOf(clt.getIdCliente()));
                    vclt.cbxNACIONALIDAD.setSelectedIndex(clt.getNacionalidad());
                    vclt.txtDNI.setText(clt.getDni() != null ? clt.getDni() : "");
                    vclt.txtEXTRANJERIA.setText(clt.getCarnetExtranjeria() != null ? clt.getCarnetExtranjeria() : "");
                    vclt.txtPASAPORTE.setText(clt.getPasaporte() != null ? clt.getPasaporte() : "");
                    if ("Si".equalsIgnoreCase(clt.getResidencia())) {
                        vclt.rbtnSI.setSelected(true);
                    } else if ("No".equalsIgnoreCase(clt.getResidencia())) {
                        vclt.rbtnNO.setSelected(true);
                    } else {
                        vclt.btnGRP_ASOCIACION.clearSelection();
                    }
                    vclt.txtN_A.setText(clt.getN_A());
                    vclt.cbxSEXO.setSelectedIndex(clt.getSexo());
                    vclt.txtLICENCIA.setText(clt.getNro_licencia());
                    vclt.txtTELEFONO.setText(clt.getTelefono());
                    vclt.txtCORREO.setText(clt.getCorreo());
                    vclt.txtDIRECCION.setText(clt.getDireccion());
                    vclt.txtDISTRITO.setText(clt.getDistrito());
                    verificarCLIENTES(vclt);
                    radioBOTONES_CLIENTE(vclt);
                    vclt.txtID_CLIENTE.setEditable(false);
                    vclt.txtID_CLIENTE.setBackground(Color.YELLOW);
                    vclt.btnREGISTRAR.setText("ACTUALIZAR");
                    vclt.jl_CLIENTE.setText("Actualización de Cliente");
                    vclt.setTitle("Actualización de Cliente ID: " + id);
                    vclt.btnLIMPIAR.setVisible(false);
                    vclt.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente con ID " + id + " no encontrado o inactivo (RAF).");
                }
                break;
            case "VEHICULO":
                Vehiculo vh = buscarVehiculoPorId_RAF(id);
                if (vh != null) {
                    vhcl.txtID_VEHICULO.setText(String.valueOf(vh.getIdVehiculo()));
                    vhcl.cbxTIPO.setSelectedItem(vh.getTipo());
                    vhcl.cbxMARCA.setSelectedItem(vh.getIdMARCA());
                    verificarCBX_MARCA(vhcl.cbxMARCA, vhcl.cbxMODELO);
                    vhcl.cbxMODELO.setSelectedItem(vh.getModelo());
                    vhcl.cbxCOLOR.setSelectedItem(vh.getColor());
                    vhcl.txtPLACA.setText(vh.getNroPLACA());
                    if ("SI".equalsIgnoreCase(vh.getLunasPOLARIZADAS())) {
                        vhcl.rbtnSI.setSelected(true);
                    } else {
                        vhcl.rbtnNO.setSelected(true);
                    }

                    vhcl.txtID_VEHICULO.setEditable(false);
                    vhcl.txtID_VEHICULO.setBackground(Color.YELLOW);
                    vhcl.btnREGISTRAR.setText("ACTUALIZAR");
                    vhcl.jl_VEHICULO.setText("Actualización de Vehículo");
                    vhcl.setTitle("Actualización de Vehículo ID: " + id);
                    vhcl.btnLIMPIAR.setVisible(false);
                    vhcl.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Vehículo con ID " + id + " no encontrado o inactivo (RAF).");
                }
                break;
            case "ENTRADA":
                REGISTRO_ENTRADAS_SALIDAS entrada = buscarEntradaPorId_RAF(id);
                if (entrada != null) {
                    vopen.txtID_ENTRADA.setText(String.valueOf(entrada.getIdEntrada()));
                    cargarCOMBO_EMPLEADO(vopen.cbxEmpleado);
                    vopen.cbxEmpleado.setSelectedItem(String.valueOf(entrada.getIdEmpleado1()));
                    cargarCOMBO_CLIENTE(vopen.cbxCLIENTE);
                    vopen.cbxCLIENTE.setSelectedItem(String.valueOf(entrada.getIdCliente1()));

                    // ----- INICIO DE LA CORRECCIÓN -----
                    // Cargar el combo de vehículos SIN filtrar para asegurar que el vehículo actual esté presente
                    cargarCOMBO_VEHICULO(vopen.cbxVehiculo, false);
                    // ----- FIN DE LA CORRECCIÓN -----

                    vopen.cbxVehiculo.setSelectedItem(String.valueOf(entrada.getIdVehiculo1()));

                    vopen.cbxPiso.setSelectedItem(String.valueOf(entrada.getPiso()));
                    vopen.txtZona.setText(entrada.getZona());
                    vopen.txtFecha.setText(entrada.getFecha_Ingreso());
                    vopen.txtHora.setText(entrada.getHora_Ingreso());
                    vopen.cbxDOCUMENTO.setSelectedItem(entrada.getTipo_Documento());
                    vopen.txtDescripcion.setText("-".equals(entrada.getDescripcion()) ? "" : entrada.getDescripcion());

                    vopen.txtID_ENTRADA.setEditable(false);
                    vopen.txtID_ENTRADA.setBackground(Color.YELLOW);
                    vopen.btnREGISTRAR.setText("ACTUALIZAR");
                    vopen.jl_ENTRADA.setText("Actualización de Entrada");
                    vopen.setTitle("Actualización de Entrada ID: " + id);
                    vopen.btnLIMPIAR.setVisible(false);
                    vopen.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Entrada con ID " + id + " no encontrada o inactiva (RAF).");
                }
                break;
            case "SALIDA":
                REGISTRO_ENTRADAS_SALIDAS salida = buscarSalidaPorId_RAF(id);
                if (salida != null) {
                    // ----- INICIO DE LA CORRECCIÓN -----
                    // Habilitar componentes antes de cargar datos para actualización
                    vexit.cbxENTRADA.setEnabled(true);
                    vexit.btnREGISTRAR.setEnabled(true);
                    vexit.btnREGISTRAR.setToolTipText("Haz click para actualizar la salida."); // Tooltip para actualizar
                    // ----- FIN DE LA CORRECCIÓN -----

                    vexit.txtID_SALIDA.setText(String.valueOf(salida.getIdSalida()));

                    // Cargar TODAS las entradas (activas o no) para poder seleccionar la que está asociada
                    vexit.cbxENTRADA.removeAllItems();
                    File archivoEntrada = new File(ENTRADA_DAT_FILE);
                    Set<String> idsEntradaUnicosParaCombo = new HashSet<>();
                    boolean entradaAsociadaEncontradaEnArchivo = false;

                    if (archivoEntrada.exists()) {
                        try (RandomAccessFile rafEnt = new RandomAccessFile(archivoEntrada, "r")) {
                            while (rafEnt.getFilePointer() < rafEnt.length()) {
                                boolean activoEnt = true;
                                int idEnt = -1;
                                try {
                                    activoEnt = rafEnt.readBoolean();
                                    idEnt = rafEnt.readInt();
                                    // Saltar el resto de los campos de la entrada
                                    rafEnt.readInt();
                                    rafEnt.readInt();
                                    rafEnt.readInt();
                                    rafEnt.readInt();
                                    readString(rafEnt, "", false);
                                    readString(rafEnt, "", false);
                                    readString(rafEnt, "", false);
                                    readString(rafEnt, "", false);
                                    readString(rafEnt, "", false);
                                } catch (IOException e) {
                                    System.err.println("Error leyendo un registro de ENTRADA.dat al poblar combo para actualizar SALIDA: " + e.getMessage());
                                    break;
                                }

                                String idEntStr = String.valueOf(idEnt);
                                if (idsEntradaUnicosParaCombo.add(idEntStr)) {
                                    vexit.cbxENTRADA.addItem(idEntStr);
                                }
                                if (idEnt == salida.getIdEntrada2()) {
                                    entradaAsociadaEncontradaEnArchivo = true;
                                }
                            }
                        } catch (IOException ex) {
                            System.err.println("Error cargando IDs de Entrada para combo en act_SALIDA: " + ex.getMessage());
                        }
                    }

                    String idEntradaAsociadaStr = String.valueOf(salida.getIdEntrada2());
                    if (!entradaAsociadaEncontradaEnArchivo) {
                        boolean yaExisteEnCombo = false;
                        for (int i = 0; i < vexit.cbxENTRADA.getItemCount(); i++) {
                            if (vexit.cbxENTRADA.getItemAt(i) != null && vexit.cbxENTRADA.getItemAt(i).equals(idEntradaAsociadaStr)) {
                                yaExisteEnCombo = true;
                                break;
                            }
                        }
                        if (!yaExisteEnCombo) {
                            vexit.cbxENTRADA.addItem(idEntradaAsociadaStr); // Añadir si no está
                            System.out.println("ADVERTENCIA: ID de Entrada " + idEntradaAsociadaStr
                                    + " (asociado a la Salida) no encontrado en ENTRADA.dat, añadido forzosamente al combo.");
                        }
                    }

                    vexit.cbxENTRADA.setSelectedItem(idEntradaAsociadaStr);
                    // Considera si el cbxENTRADA debe ser no editable durante la actualización de una salida
                    // vexit.cbxENTRADA.setEnabled(false); 

                    cargar_idVehiculo(vexit.cbxENTRADA, vexit.txtVEHICULO);
                    cargar_idCliente(vexit.cbxENTRADA, vexit.txtCLIENTE);

                    cargarCOMBO_EMPLEADO(vexit.cbxEmpleado);
                    vexit.cbxEmpleado.setSelectedItem(String.valueOf(salida.getIdEmpleado2()));

                    vexit.txtFecha.setText(salida.getFecha_Salida());
                    vexit.txtHora.setText(salida.getHora_Salida());
                    vexit.txtDescripcion.setText("-".equals(salida.getDescripcion2()) ? "" : salida.getDescripcion2());

                    vexit.txtID_SALIDA.setEditable(false);
                    vexit.txtID_SALIDA.setBackground(Color.YELLOW);
                    vexit.btnREGISTRAR.setText("ACTUALIZAR");
                    vexit.jl_SALIDA.setText("Actualización de Salida");
                    vexit.setTitle("Actualización de Salida ID: " + id);
                    vexit.btnLIMPIAR.setVisible(false);
                    // vexit.btnREGISTRAR.setEnabled(true); // Ya se hizo arriba
                    vexit.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Salida con ID " + id + " no encontrada o inactiva (RAF).");
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Actualización para tipo '" + tipo + "' con RAF pendiente.");
                break;
        }
    }

    @Deprecated
    public void crearARCHIVO() {
        /* No usado con RAF */ }

    @Deprecated
    public void anadirEMPLEADO(Empleado emp) {
        /* Usar _RAF */ }

    public void salir() {
        System.exit(0);
    }

    public void cargarIDsParaBuscar(JComboBox cbxTIPO, JComboBox cbxID) {
        cbxID.removeAllItems(); // Limpiar JComboBox de IDs
        if (cbxTIPO.getSelectedIndex() == -1 || cbxTIPO.getSelectedItem() == null) {
            // No hay tipo seleccionado, no hacer nada o deshabilitar cbxID
            cbxID.setEnabled(false);
            return;
        }
        cbxID.setEnabled(true);

        String tipoSeleccionado = cbxTIPO.getSelectedItem().toString().toUpperCase();
        String archivoDatNombre = BASE_DATA_PATH + tipoSeleccionado + ".dat";
        File archivoDat = new File(archivoDatNombre);

        if (!archivoDat.exists()) {
            JOptionPane.showMessageDialog(null, "No existen registros de tipo '" + tipoSeleccionado + "' (RAF).", "Archivo No Encontrado", JOptionPane.INFORMATION_MESSAGE);
            cbxID.setEnabled(false);
            return;
        }

        boolean cargadoAlgunID = false;
        try (RandomAccessFile raf = new RandomAccessFile(archivoDat, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean activo = raf.readBoolean();
                int id = raf.readInt();

                // Saltar el resto de los campos del registro específico del tipo
                // Es crucial que este salto sea correcto para cada tipo de entidad.
                switch (tipoSeleccionado) {
                    case "EMPLEADO":
                        raf.readInt(); // nacionalidad
                        readString(raf, "-", false); // dni
                        readString(raf, "-", false); // carnetExtranjeria
                        readString(raf, "-", false); // pasaporte
                        readString(raf, "-", false); // residencia
                        readString(raf, "Sin Nombre", false); // n_a
                        raf.readInt(); // sexo
                        readString(raf, "00/00/0000", false); // fecha_nacimiento
                        readString(raf, "-", false); // telefono
                        readString(raf, "-", false); // correo
                        readString(raf, "-", false); // direccion
                        readString(raf, "-", false); // distrito
                        break;
                    case "CLIENTE":
                        raf.readInt(); // nacionalidad
                        readString(raf, "-", false); // dni
                        readString(raf, "-", false); // carnetExtranjeria
                        readString(raf, "-", false); // pasaporte
                        readString(raf, "-", false); // residencia
                        readString(raf, "Sin Nombre", false); // n_a
                        raf.readInt(); // sexo
                        readString(raf, "-", false); // nro_licencia
                        readString(raf, "-", false); // telefono
                        readString(raf, "-", false); // correo
                        readString(raf, "-", false); // direccion
                        readString(raf, "-", false); // distrito
                        break;
                    case "VEHICULO":
                        readString(raf, "No especificado", false); // tipo
                        readString(raf, "Desconocida", false);    // marca
                        readString(raf, "Desconocido", false);    // modelo
                        readString(raf, "No especificado", false); // color
                        readString(raf, "-", false);               // nroPlaca
                        readString(raf, "NO", false);              // lunasPolarizadas
                        break;
                    case "ENTRADA":
                        raf.readInt(); // idEmpleado1
                        raf.readInt(); // idCliente1
                        raf.readInt(); // idVehiculo1
                        raf.readInt(); // piso
                        readString(raf, "-", false); // zona
                        readString(raf, "00/00/0000", false); // fecha_Ingreso
                        readString(raf, "00:00", false);      // hora_Ingreso
                        readString(raf, "Boleta", false);    // tipo_Documento
                        readString(raf, "-", false);          // descripcion
                        break;
                    case "SALIDA":
                        raf.readInt(); // idEntrada2
                        raf.readInt(); // idEmpleado2
                        raf.readInt(); // idCliente2
                        raf.readInt(); // idVehiculo2
                        readString(raf, "00/00/0000", false); // fecha_Salida
                        readString(raf, "00:00", false);      // hora_Salida
                        readString(raf, "-", false);          // descripcion2
                        break;
                    default:
                        // Si el tipo no es conocido, no podemos saber cuántos bytes saltar.
                        // Esto podría ser un problema. Idealmente, todos los tipos deben estar cubiertos.
                        System.err.println("Tipo desconocido en cargarIDsParaBuscar: " + tipoSeleccionado);
                        // Para evitar un bucle infinito si el salto es incorrecto, podríamos romper aquí
                        // o lanzar una excepción, pero por ahora solo imprimimos error.
                        break;
                }

                if (activo) {
                    cbxID.addItem(String.valueOf(id));
                    cargadoAlgunID = true;
                }
            }
            if (!cargadoAlgunID) {
                JOptionPane.showMessageDialog(null, "No hay " + tipoSeleccionado + "s activos para seleccionar (RAF).");
                cbxID.setEnabled(false); // Deshabilitar si no hay IDs
            } else {
                cbxID.setSelectedIndex(-1); // Dejar sin selección si hay IDs
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar IDs de " + tipoSeleccionado + " (RAF).\n" + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            cbxID.setEnabled(false);
        }
    }

}
