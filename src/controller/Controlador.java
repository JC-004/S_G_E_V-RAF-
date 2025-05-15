package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import service.Service;
import vista.Vista;
import vista.mostrarCLIENTES;
import vista.mostrarEMPLEADOS;
import vista.mostrarENTRADAS;
import vista.mostrarSALIDAS;
import vista.mostrarVEHICULOS;
import vista.vistaACTUALIZAR;
import vista.vistaBUSCAR;
import vista.vistaCLIENTE;
import vista.vistaELIMINAR;
import vista.vistaEMPLEADO;
import vista.vistaENTRADA;
import vista.vistaRESULTADOS;
import vista.vistaSALIDA;
import vista.vistaVEHICULO;

public class Controlador implements ActionListener {

    //vista principal
    Vista vista = new Vista();
    vistaEMPLEADO vemp = new vistaEMPLEADO();
    vistaCLIENTE vclt = new vistaCLIENTE();
    vistaVEHICULO vhcl = new vistaVEHICULO();
    vistaSALIDA vsld = new vistaSALIDA();
    //----------------------------------------
    //habilitar la vista a la tabla EMPLEADO
    mostrarEMPLEADOS mEMP = new mostrarEMPLEADOS();
    //habilitar la vista a la tabla CLIENTE
    mostrarCLIENTES mCLT = new mostrarCLIENTES();
    //habilitar la vista a la tabla VEHICULOS
    mostrarVEHICULOS mvhcl = new mostrarVEHICULOS();

    //habilitar la vista al REGISTRO DE ENTRADA VEHICULO
    vistaENTRADA veta = new vistaENTRADA();

    //habilitar la vista a 'mostrarENTRADAS'
    mostrarENTRADAS mETA = new mostrarENTRADAS();
    //habilitar la vista a 'mostrarSALIDAS'
    mostrarSALIDAS msld = new mostrarSALIDAS();

    //VISTA PARA ELIMINAR
    vistaELIMINAR vdlt = new vistaELIMINAR();

    //VISTA PARA BUSCAR
    vistaBUSCAR vbcr = new vistaBUSCAR();

    //VISTA PARA MOSTRAR BUSQUEDA
    vistaRESULTADOS vrts = new vistaRESULTADOS();
    //vista para actualizar
    vistaACTUALIZAR update = new vistaACTUALIZAR();

    Service service = new Service();

    public Controlador(Vista vista, vistaEMPLEADO vemp, vistaCLIENTE vclt, vistaVEHICULO vhcl, vistaENTRADA veta, vistaSALIDA vsld,
            mostrarEMPLEADOS mEMP, mostrarCLIENTES mCLT, mostrarVEHICULOS mvhcl,
            mostrarENTRADAS mETA, mostrarSALIDAS msld,
            vistaELIMINAR vdlt, vistaBUSCAR vbcr, vistaRESULTADOS vrts,
            vistaACTUALIZAR update
    ) {
        this.vista = vista;
        this.vemp = vemp;
        this.vclt = vclt;
        this.vhcl = vhcl;
        this.veta = veta;
        this.vsld = vsld;
        //instanciamos this para mostrar los empleados
        this.mEMP = mEMP;
        //instancioamos this para mostrar los clientes
        this.mCLT = mCLT;
        //instanciamos this para mostrar los vehiculos
        this.mvhcl = mvhcl;
        //instanciamos this para mostrar las entradas
        this.mETA = mETA;
        //instanciamos this para mostrar las salidas
        this.msld = msld;

        //instanciamos a la ventana de eliminar
        this.vdlt = vdlt;
        //instanciamos a la ventana de buscar
        this.vbcr = vbcr;
        this.vrts = vrts;
        //instanciamos a la ventana de buscar
        this.update = update;

        /*Habilitar las opciones a vista PRINCIPAL
        habilitar el registro de ENTRADA 'VEHICULO' */
        vista.Entrada.addActionListener(this);
        vista.Salida.addActionListener(this);

        //habilitar opciones a los menus
        vista.Empleado.addActionListener(this);
        vista.Cliente.addActionListener(this);
        vista.Vehiculo.addActionListener(this);
        vista.Salir.addActionListener(this);

        //habilitar las opciones a los menus de 'MOSTRAR'
        vista.mostrarEMPLEADOS.addActionListener(this);
        vista.mostrarCLIENTES.addActionListener(this);
        vista.mostrarVEHICULOS.addActionListener(this);

        //HABILITAR LAS OPCIONES A LA VISTA DE 'MOSTRAR EMPLEADOS'
        mEMP.btnREGRESAR.addActionListener(this);

        //HABILITAR LAS OPCIONES A LA VISTA DE 'MOSTRAR CLIENTES'
        mCLT.btnREGRESAR.addActionListener(this);

        //HABILITAR LAS OPCIONES A LA VISTA DE 'MOSTRAR VEHICULOS'
        mvhcl.btnREGRESAR.addActionListener(this);

        //HABILITAR LAS OPCIONES A LA VISTA DE 'MOSTRAR ENTRADAS'
        mETA.btnREGRESAR.addActionListener(this);
        //HABILITAR LAS OPCIONES A LA VISTA DE 'MOSTRAR SALIDAS'
        msld.btnREGRESAR.addActionListener(this);

        //--------------------------------------------------
        //habilitar las opciones a vistaEMPLEADO
        vemp.btnREGRESAR.addActionListener(this);
        vemp.btnREGISTRAR.addActionListener(this);

        vemp.cbxNACIONALIDAD.addActionListener(this);

        vemp.rbtnSI.addActionListener(this);
        vemp.rbtnNO.addActionListener(this);

        vemp.btnLIMPIAR.addActionListener(this);

        //---------------------------------------------------
        //habilitar las opciones a vistaCLIENTE
        vclt.btnREGRESAR.addActionListener(this);
        vclt.btnREGISTRAR.addActionListener(this);

        vclt.cbxNACIONALIDAD.addActionListener(this);

        vclt.rbtnSI.addActionListener(this);
        vclt.rbtnNO.addActionListener(this);
        vclt.btnLIMPIAR.addActionListener(this);
        //---------------------------------------------------
        //habilitar las opciones a vistaVEHICULO
        vhcl.btnREGRESAR.addActionListener(this);
        vhcl.btnREGISTRAR.addActionListener(this);
        vhcl.cbxMARCA.addActionListener(this);
        vhcl.cbxMODELO.addActionListener(this);
        vhcl.btnLIMPIAR.addActionListener(this);

        //habilitar opciones a la ventana 'vistaENTRADA'
        veta.btnREGRESAR.addActionListener(this);
        veta.btnLIMPIAR.addActionListener(this);
        veta.btnREGISTRAR.addActionListener(this);

        //HABILITAR LAS OPCIONES A LA VISTA -> 'mostrarENTRADAS'
        vista.mostrarENTRADAS.addActionListener(this);

        //---------------------------------------------------
        //habilitar opciones a la ventana 'vistaSALIDA'
        vsld.btnREGISTRAR.addActionListener(this);
        vsld.btnLIMPIAR.addActionListener(this);
        vsld.btnREGRESAR.addActionListener(this);
        vsld.cbxENTRADA.addActionListener(this);

        //HABILITAR LAS OPCIONES A LA VISTA -> 'mostrarSALIDAS'
        vista.mostrarSALIDAS.addActionListener(this);

        //--------------------------------------------------------------
        //habilitar las opciones a la vista -> ELIMINAR
        vista.eliminarMN.addActionListener(this);
        //habilitar las opciones a VDLT        
        vdlt.cbxTIPO.addActionListener(this);
        vdlt.cbxID.addActionListener(this);

        vdlt.btnELIMINAR.addActionListener(this);
        vdlt.btnREGRESAR.addActionListener(this);

        //habilitar las opciones a la vista -> BUSCAR
        vista.buscarMN.addActionListener(this);
        //habilitar las opciones a vbcr

        vbcr.cbxTIPO.addActionListener(this);
        vbcr.cbxID.addActionListener(this);

        vbcr.btnBUSCAR.addActionListener(this);
        vbcr.btnREGRESAR.addActionListener(this);

        //-----------------------VISTA RESULTADOS---------------
        vrts.btnREGRESAR.addActionListener(this);

        //------------------------VISTA -> ACTUALIZAR MENU----------------------
        vista.actualizarMN.addActionListener(this);

        //---------------------ACTUALIZAR -> INTERFAZ
        update.btnREGRESAR.addActionListener(this);
        update.btnACTUALIZAR.addActionListener(this);
        update.cbxTIPO.addActionListener(this);
        update.cbxID.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        //-----------------VENTANA EMPLEADO-------------------------------------                        
        //"REGISTRAR"->EMPLEADO
        if (ae.getSource() == vista.Empleado) {
            vemp.btnLIMPIAR.setVisible(true);
            service.limpiarEMPLEADO(vemp);
            vemp.jl_EMPLEADO.setText("Registro de Empleado");
            vemp.setVisible(true);

        } //----------------------------------------------------------------------
        //"cbxNACIONALIDAD" 
        else if (ae.getSource() == vemp.cbxNACIONALIDAD) {
            service.verificarEMPLEADOS(vemp);
        } //" RADIO BOTONES 'SI' y 'NO' "
        else if (ae.getSource() == vemp.rbtnSI || ae.getSource() == vemp.rbtnNO) {
            service.radioBOTONES(vemp);
        } //"btnREGRESAR"
        else if (ae.getSource() == vemp.btnREGRESAR) {
            vemp.dispose();
        }//"btnREGISTRAR"        
        else if (ae.getSource() == vemp.btnREGISTRAR) { // El mismo botón cambia de texto
            // Verificar qué dice el botón
            if (vemp.btnREGISTRAR.getText().equals("REGISTRAR")) {

                // Si dice REGISTRAR, llama a la lógica de validación y guardado original
                System.out.println("DEBUG (Controller): Botón Registrar Empleado presionado.");
                service.verificarCAMPOS(vemp); // Tu método original para añadir nuevo
                if (mEMP.isVisible()) {
                    service.disenoTABLA_EMPLEADOS(mEMP.tablaEMPLEADOS);
                }

            } else if (vemp.btnREGISTRAR.getText().equals("ACTUALIZAR")) {
                // Si dice ACTUALIZAR, llama a la nueva lógica de actualización
                System.out.println("DEBUG (Controller): Botón ACTUALIZAR Empleado presionado.");
                // --- LLAMADA AL NUEVO MÉTODO DEL SERVICIO ---
                boolean actualizado = service.procesarActualizacionEmpleado(vemp);
                if (actualizado) {
                    vemp.dispose(); // Cierra la ventana si la actualización fue exitosa
                    // Opcional: Refrescar tabla de mostrar empleados si está visible
                    if (mEMP.isVisible()) {
                        service.disenoTABLA_EMPLEADOS(mEMP.tablaEMPLEADOS);
                    }
                }
                // Si no fue exitoso, el método del servicio ya mostró el error
            }
        } else if (ae.getSource() == vemp.btnLIMPIAR) {
            service.limpiarEMPLEADO(vemp);
        }

        //-----------------VENTANA CLIENTE--------------------------------------                        
        //"REGISTRAR" ->CLIENTE
        if (ae.getSource() == vista.Cliente) {
            vclt.btnLIMPIAR.setVisible(true);
            service.limpiarCLIENTE(vclt);
            vclt.jl_CLIENTE.setText("Registro de cliente");
            vclt.setVisible(true);
        } //----------------------------------------------------------------------
        //"cbxNACIONALIDAD" 
        else if (ae.getSource() == vclt.cbxNACIONALIDAD) {
            service.verificarCLIENTES(vclt);
        } //" RADIO BOTONES 'SI' y 'NO' "
        else if (ae.getSource() == vclt.rbtnSI || ae.getSource() == vclt.rbtnNO) {
            service.radioBOTONES_CLIENTE(vclt);
        } //"btnREGRESAR"
        else if (ae.getSource() == vclt.btnREGRESAR) {

            vclt.dispose();

        }//"btnREGISTRAR"
        else if (ae.getSource() == vclt.btnREGISTRAR) {

            // Verificar el texto del botón
            if (vclt.btnREGISTRAR.getText().equals("REGISTRAR")) {
                // Si dice REGISTRAR, llama a la lógica de registro original
                System.out.println("DEBUG (Controller): Botón Registrar Cliente presionado.");
                service.verificarCAMPOS(vclt); // Tu método original de añadir
                if (mCLT.isVisible()) {
                    service.disenoTABLA_CLIENTES(mCLT.tablaCLIENTES);
                }
            } else if (vclt.btnREGISTRAR.getText().equals("ACTUALIZAR")) {

                // Si dice ACTUALIZAR, llama a la nueva lógica de actualización
                System.out.println("DEBUG (Controller): Botón ACTUALIZAR Cliente presionado.");
                boolean actualizado = service.procesarActualizacionCliente(vclt); // <--- LLAMADA AL NUEVO MÉTODO
                if (actualizado) {
                    vclt.dispose(); // Cierra la ventana si la actualización fue exitosa
                    // Opcional: actualizar tabla de mostrar clientes si está visible
                    if (mCLT.isVisible()) {
                        service.disenoTABLA_CLIENTES(mCLT.tablaCLIENTES);
                    }
                }
                // Si no fue exitoso, el método del servicio ya mostró el error
            }

        } else if (ae.getSource() == vclt.btnLIMPIAR) {

            service.limpiarCLIENTE(vclt);
        }

        //-----------------VENTANA VECHICULO--------------------------------------                        
        //"REGISTRAR" ->VEHICULO
        if (ae.getSource() == vista.Vehiculo) {
            service.limpiarVEHICULO(vhcl); // <-- LLAMAR A LIMPIAR/RESETEAR
            vhcl.jl_VEHICULO.setText("Registro de Vehiculo");
            vhcl.btnLIMPIAR.setVisible(true);
            vhcl.setVisible(true);
        } else if (ae.getSource() == vhcl.cbxMARCA) {

            service.verificarCBX_MARCA(vhcl.cbxMARCA, vhcl.cbxMODELO);

        } else if (ae.getSource() == vhcl.btnREGISTRAR) { // Botón en vistaVEHICULO
            // Verificar texto del botón
            if (vhcl.btnREGISTRAR.getText().equals("REGISTRAR")) {
                System.out.println("DEBUG (Controller): Botón Registrar Vehículo presionado.");
                service.verificarCAMPOS_RVEHICULOS(vhcl); // Llama a tu validación/guardado original
                if (mvhcl.isVisible()) {
                    service.disenoTABLA_VEHICULOS(mvhcl.tablaVEHICULOS);
                }

            } else if (vhcl.btnREGISTRAR.getText().equals("ACTUALIZAR")) {
                System.out.println("DEBUG (Controller): Botón ACTUALIZAR Vehículo presionado.");
                boolean actualizado = service.procesarActualizacionVehiculo(vhcl); // <-- LLAMADA AL NUEVO MÉTODO
                if (actualizado) {
                    vhcl.dispose(); // Cierra si éxito
                    // Opcional: refrescar tabla de vehículos si está visible
                    if (mvhcl.isVisible()) {
                        service.disenoTABLA_VEHICULOS(mvhcl.tablaVEHICULOS);
                    }
                }
                // Si no, el servicio ya mostró error
            }
        } else if (ae.getSource() == vhcl.btnREGRESAR) {
            vhcl.dispose();
        } else if (ae.getSource() == vhcl.btnLIMPIAR) {
            service.limpiarVEHICULO(vhcl);
        }

        //-----------------MOSTRAR EMPLEADOS--------------------------------------
        if (ae.getSource() == vista.mostrarEMPLEADOS) {
            service.disenoTABLA_EMPLEADOS(mEMP.tablaEMPLEADOS);
            mEMP.setVisible(true);

        } else if (ae.getSource() == mEMP.btnREGRESAR) {
            mEMP.dispose();
        }

        //-----------------MOSTRAR CLIENTES--------------------------------------
        if (ae.getSource() == vista.mostrarCLIENTES) {
            service.disenoTABLA_CLIENTES(mCLT.tablaCLIENTES);
            mCLT.setVisible(true);
        } else if (ae.getSource() == mCLT.btnREGRESAR) {
            mCLT.dispose();
        }

        //-----------------MOSTRAR VEHICULOS--------------------------------------
        if (ae.getSource() == vista.mostrarVEHICULOS) {
            service.disenoTABLA_VEHICULOS(mvhcl.tablaVEHICULOS);
            mvhcl.setVisible(true);
        } else if (ae.getSource() == mvhcl.btnREGRESAR) {
            mvhcl.dispose();
        }

        //--------------VENTANA DE REGISTRO DE 'ENTRADA'------------------
        if (ae.getSource() == vista.Entrada) {

            service.limpiarENTRADA(veta);

            veta.jl_ENTRADA.setText("Registro de Entrada");
            veta.btnLIMPIAR.setVisible(true);
            veta.setVisible(true);
            service.cargarCOMBO_CLIENTE(veta.cbxCLIENTE);
            service.cargarCOMBO_EMPLEADO(veta.cbxEmpleado);

            service.cargarCOMBO_VEHICULO(veta.cbxVehiculo, true);

        } else if (ae.getSource() == veta.btnREGRESAR) {
            veta.dispose();
        } else if (ae.getSource() == veta.btnLIMPIAR) {
            service.limpiarENTRADA(veta);

        } else if (ae.getSource() == veta.btnREGISTRAR) {
            //service.verificarCAMPO_ENTRADA(veta);

            // Verificar texto del botón
            if (veta.btnREGISTRAR.getText().equals("REGISTRAR")) {
                System.out.println("DEBUG (Controller): Botón Registrar Entrada presionado.");
                service.verificarCAMPO_ENTRADA(veta); // Tu método original para añadir

                //refresar el combo entrada
                service.cargarCOMBO_VEHICULO(veta.cbxVehiculo, true);
                if (mETA.isVisible()) {
                    service.disenoTABLA_ENTRADAS(mETA.tablaENTRADAS);
                }
            } else if (veta.btnREGISTRAR.getText().equals("ACTUALIZAR")) {
                System.out.println("DEBUG (Controller): Botón ACTUALIZAR Entrada presionado.");
                boolean actualizado = service.procesarActualizacionEntrada(veta); // <-- LLAMADA AL NUEVO MÉTODO
                if (actualizado) {
                    service.limpiarENTRADA(veta); // Limpiar/Resetear después de actualizar con éxito
                    veta.dispose(); // Cierra si éxito
                    // Opcional: Refrescar tabla de entradas si está visible
                    if (mETA.isVisible()) {
                        service.disenoTABLA_ENTRADAS(mETA.tablaENTRADAS);
                    }
                }

            }

        }

        //-------------------mostrarENTRADA------------
        if (ae.getSource() == vista.mostrarENTRADAS) {
            service.disenoTABLA_ENTRADAS(mETA.tablaENTRADAS);
            mETA.setVisible(true);
        } else if (ae.getSource() == mETA.btnREGRESAR) {
            //service.limpiar_ENTRADA(veta); // <-- Limpiar/Resetear al regresar
            mETA.dispose();
        }

        //--------------VENTANA DE REGISTRO DE 'SALIDA'------------------
        if (ae.getSource() == vista.Salida) {

            service.limpiarCAMPOS_SALIDA(vsld);

            vsld.btnLIMPIAR.setVisible(true);

            //INICIO DE LA CORRECION
            vsld.cbxENTRADA.setEnabled(true);
            vsld.btnREGISTRAR.setEnabled(true);
            vsld.btnREGISTRAR.setToolTipText("Haz click para registrar la salida.");

            vsld.setVisible(true);
            service.cargarCOMBO_EMPLEADO(vsld.cbxEmpleado);
            service.cargarCOMBO_ENTRADA(vsld.cbxENTRADA, vsld.btnREGISTRAR);

            //*** AQUÍ puedes añadir la mejora OPCIONAL ***
        } else if (ae.getSource() == vsld.cbxENTRADA) { // Cuando seleccionas una Entrada
            Object selectedItem = vsld.cbxENTRADA.getSelectedItem();

            if (selectedItem != null) { // Si se seleccionó algo válido
                String idEntradaSeleccionado = selectedItem.toString();

                // --- *** NUEVO: Actualizar txtID_SALIDA *** ---
                vsld.txtID_SALIDA.setText(idEntradaSeleccionado); // Poner el mismo ID
                vsld.txtID_SALIDA.setEditable(false);             // Hacer no editable
                vsld.txtID_SALIDA.setBackground(Color.YELLOW);    // Poner fondo amarillo
                // --- *** FIN NUEVO *** ---

                // Cargar los datos asociados (como ya lo hacías)
                service.cargar_idVehiculo(vsld.cbxENTRADA, vsld.txtVEHICULO);
                service.cargar_idCliente(vsld.cbxENTRADA, vsld.txtCLIENTE);
                // --- *** NUEVO: Llamar a cargar_idEMPLEADO *** ---
                service.cargar_idEMPLEADO(vsld.cbxENTRADA, vsld.cbxEmpleado); // Selecciona el empleado

            } else { // Si no hay nada seleccionado (o se deselecciona)
                // Limpiar y restablecer txtID_SALIDA
                vsld.txtID_SALIDA.setText("");
                //vsld.txtID_SALIDA.setEditable(true); // Hacer editable de nuevo
                //vsld.txtID_SALIDA.setBackground(Color.WHITE); // Fondo blanco (o el original)

                // Limpiar los otros campos asociados
                vsld.txtVEHICULO.setText("");
                vsld.txtCLIENTE.setText("");
                vsld.cbxEmpleado.setSelectedIndex(-1); // Deseleccionar empleado
            }

        } else if (ae.getSource() == vsld.btnREGISTRAR) {
            //service.verificarCAMPOS_SALIDA(vsld); 
            if (vsld.btnREGISTRAR.getText().equals("REGISTRAR")) {
                System.out.println("DEBUG (Controller): Botón Registrar Salida presionado.");
                service.verificarCAMPOS_SALIDA(vsld); // Tu método original
                if (msld.isVisible()) {
                    service.disenoTABLA_SALIDAS(msld.tablaSALIDAS);
                }
            } else if (vsld.btnREGISTRAR.getText().equals("ACTUALIZAR")) {
                System.out.println("DEBUG (Controller): Botón ACTUALIZAR Salida presionado.");
                boolean actualizado = service.procesarActualizacionSalida(vsld); // <-- LLAMADA AL NUEVO MÉTODO
                if (actualizado) {
                    service.limpiarCAMPOS_SALIDA(vsld); // Limpiar/Resetear después de actualizar
                    vsld.dispose(); // Cierra si éxito
                    // Opcional: Refrescar tabla de salidas si está visible
                    if (msld.isVisible()) {
                        service.disenoTABLA_SALIDAS(msld.tablaSALIDAS);
                    }
                }
                // Si no, el servicio ya mostró error
            }

        } else if (ae.getSource() == vsld.btnLIMPIAR) {
            service.limpiarCAMPOS_SALIDA(vsld);
        } else if (ae.getSource() == vsld.btnREGRESAR) {
            //service.limpiarCAMPOS_SALIDA(vsld); // <-- Limpiar/Resetear al regresar
            vsld.dispose();
        }

        //--------------------------------MOSTRAR SALIDAS-------------------------------
        if (ae.getSource() == vista.mostrarSALIDAS) {
            service.disenoTABLA_SALIDAS(msld.tablaSALIDAS);
            msld.setVisible(true);
        } else if (ae.getSource() == msld.btnREGRESAR) {
            msld.dispose();
        }

        //-----------------------------------VENTANA DE ELIMINACION-------------------------
        if (ae.getSource() == vista.eliminarMN) {
            vdlt.setVisible(true);

        } else if (ae.getSource() == vdlt.cbxTIPO) {
            service.cargarIdsParaEliminar(vdlt.cbxTIPO, vdlt.cbxID);
        } else if (ae.getSource() == vdlt.btnELIMINAR) {
            //aca lo vamos a eliminar
            service.solicitarConfirmacionYEliminar(vdlt.cbxTIPO, vdlt.cbxID);
            if (mEMP.isVisible()) {

                service.disenoTABLA_EMPLEADOS(mEMP.tablaEMPLEADOS);
            }
            if (mCLT.isVisible()) {

                service.disenoTABLA_CLIENTES(mCLT.tablaCLIENTES);
            }
            if (mvhcl.isVisible()) {
                service.disenoTABLA_VEHICULOS(mvhcl.tablaVEHICULOS);
            }
            if (mETA.isVisible()) {
                service.disenoTABLA_ENTRADAS(mETA.tablaENTRADAS);
            }
            if (msld.isVisible()) {
                service.disenoTABLA_SALIDAS(msld.tablaSALIDAS);
            }

        } else if (ae.getSource() == vdlt.btnREGRESAR) {
            vdlt.dispose();
        }

        //-----------------------------------VENTANA DE BUSCAR-------------------------
        if (ae.getSource() == vista.buscarMN) {
            vbcr.setVisible(true);
        } else if (ae.getSource() == vbcr.cbxTIPO) {
            service.cargarIDsParaBuscar(vbcr.cbxTIPO, vbcr.cbxID);

        } else if (ae.getSource() == vbcr.btnBUSCAR) {
            service.cargarDATOS_BUSQUEDA(vbcr.cbxTIPO, vbcr.cbxID, vrts.tablaRESULTADOS, vrts.txtESCRIBIR, vrts);

        } else if (ae.getSource() == vrts.btnREGRESAR) {
            vrts.dispose();
        } else if (ae.getSource() == vbcr.btnREGRESAR) {
            vbcr.dispose();
        }

        //-----------------VENTANA DE ACTUALIZACION-------------------------------------
        if (ae.getSource() == vista.actualizarMN) {
            update.setVisible(true);
        } else if (ae.getSource() == update.cbxTIPO) {
            service.cargarIDsParaBuscar(update.cbxTIPO, update.cbxID);
        } else if (ae.getSource() == update.btnACTUALIZAR) {

            service.actualizar(vemp, vclt, vhcl, veta, vsld, update.cbxTIPO, update.cbxID);
        } else if (ae.getSource() == update.btnREGRESAR) {
            update.dispose();
        }

        //si el usuario hace click en el menu "REGISTRAR"->SALIR
        if (ae.getSource() == vista.Salir) {
            service.salir();

        }

    }

}
