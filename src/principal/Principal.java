package principal;

import vista.Login;

public class Principal {

    /*
    Sistema de gestion de estacionamiento
    de vehiculos.
    Desarrollar un Sistema de Gestion de Parqueo/ Estacionamiento de Vehiculos
    El sistema debe permitir la operacion y mantenimiento de los siguientes
    modulos
    °tablas generales
    °vehiculos
    °empleados
    °registros E/S
    °etc...                
     */
    public static void main(String[] args) {
        System.out.println("Iniciando la aplicacion");
        Login login = new Login();

        login.setVisible(true);

//        Vista vista = new Vista();
//        vistaEMPLEADO vemp = new vistaEMPLEADO();
//        vistaCLIENTE vclt = new vistaCLIENTE();
//        vistaVEHICULO vhcl = new vistaVEHICULO();
//        vistaENTRADA veta = new vistaENTRADA();
//        vistaSALIDA vsld = new vistaSALIDA();
//        mostrarEMPLEADOS mEMP = new mostrarEMPLEADOS();
//        mostrarCLIENTES mCLT = new mostrarCLIENTES();
//        mostrarVEHICULOS mvhcl = new mostrarVEHICULOS();
//        mostrarENTRADAS mETA = new mostrarENTRADAS();
//        mostrarSALIDAS msld = new mostrarSALIDAS();
//        vistaELIMINAR vdlt = new vistaELIMINAR();
//        vistaBUSCAR vbcr = new vistaBUSCAR();
//        vistaRESULTADOS vrts = new vistaRESULTADOS();
//        vistaACTUALIZAR update = new vistaACTUALIZAR();
//
//        Controlador ctr = new Controlador(vista, vemp, vclt, vhcl, veta, vsld, mEMP,
//                mCLT, mvhcl, mETA, msld, vdlt, vbcr, vrts, update);
//
//        vista.setVisible(true);
    }

}
