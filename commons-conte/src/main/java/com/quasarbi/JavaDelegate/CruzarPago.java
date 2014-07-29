package com.quasarbi.JavaDelegate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import com.quasarbi.ConexionBD.Conexion;

public class CruzarPago implements JavaDelegate {

	private Conexion conn;
	private Expression numeroFormulario;
	private Expression numeroDocumento;//, fechaRadicacion;
	private BigDecimal valorPagoRealizado, valorPagoRealizado2 = null;
	private BigDecimal valorAPagar = null;
	private int tipoSolicitud;
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		this.conn = new Conexion();
		System.out.print("\n\nINGRESA AL MÉTODO QUE VA A VALIDAR EL PAGO\n\n");
		this.conn.getDataSource();
		String solicitudId = (String)this.numeroFormulario.getValue(arg0);
		String documento = (String)this.numeroDocumento.getValue(arg0);
		this.valorPagoRealizado = this.conn.compruebaPago(solicitudId, documento);
		System.out.print("\n\nSE VA A VALIDAR SI valorPagoRealizado ES NULL, EL VALOR ES "+valorPagoRealizado+"\n\n");
		if(this.valorPagoRealizado!=null) {
			System.out.print("\n\nEN EL IF\n\n");
			this.tipoSolicitud = this.conn.obtieneTipoSolicitud((String)this.numeroFormulario.getValue(arg0));
			this.valorPagoRealizado = this.valorPagoRealizado.setScale(0, RoundingMode.DOWN);
			if(this.tipoSolicitud!=-1){
				this.valorAPagar = this.conn.obtieneValorAPagarxSolicitud(this.tipoSolicitud);
				this.valorAPagar = this.valorAPagar.setScale(0, RoundingMode.DOWN);
				int estadoPago = this.valorPagoRealizado.compareTo(this.valorAPagar);
				
				switch(estadoPago){				
				case -1:
					this.valorPagoRealizado2 = this.conn.compruebaPago2(solicitudId, documento);
					arg0.setVariable("estadoPago", "Menor");
					String observaciones = (String)arg0.getVariable("observaciones");
					observaciones = observaciones + "\nPago incompleto.\n";
					arg0.setVariable("observaciones", observaciones);
					System.out.print("\n\nINGRESO AL CASE -1\n\n");
					break;
				case 0:
					arg0.setVariable("estadoPago", "Completo");
					System.out.print("\n\nINGRESO AL CASE 0\n\n");
					break;
				case 1:
					arg0.setVariable("estadoPago", "Completo");
					System.out.print("\n\nINGRESO AL CASE 1\n\n");
					break;
				}
				System.out.print("\n\n\n EL VALOR DEL PAGO REALIZADO ES => " + valorPagoRealizado + "y el valor a pagar es " + valorAPagar + "y el estadoPago es " + estadoPago + "\n\n\n");
			}
		} else if(this.valorPagoRealizado==null) {
			System.out.print("\n\nEN EL ELSE IF\n\n");
			this.valorPagoRealizado = this.conn.compruebaPago_(solicitudId, documento);
			if(this.valorPagoRealizado!=null) {
				this.tipoSolicitud = this.conn.obtieneTipoSolicitud((String)this.numeroFormulario.getValue(arg0));
				this.valorPagoRealizado = this.valorPagoRealizado.setScale(0, RoundingMode.DOWN);
				if(this.tipoSolicitud!=-1){
					this.valorAPagar = this.conn.obtieneValorAPagarxSolicitud(this.tipoSolicitud);
					this.valorAPagar = this.valorAPagar.setScale(0, RoundingMode.DOWN);
					int estadoPago = this.valorPagoRealizado.compareTo(this.valorAPagar);
					
					switch(estadoPago){
					case -1:
						this.valorPagoRealizado2 = this.conn.compruebaPago2(solicitudId, documento);
						arg0.setVariable("estadoPago", "Menor");
						String observaciones = (String)arg0.getVariable("observaciones");
						observaciones = observaciones + "\nPago incompleto.\n";
						arg0.setVariable("observaciones", observaciones);
						System.out.print("\n\nINGRESO AL CASE DE ELSE IF -1\n\n");
						break;
					case 0:
						arg0.setVariable("estadoPago", "Completo");
						conn.moverAPago(solicitudId, documento);
						System.out.print("\n\nINGRESO AL CASE DE ELSE IF 0\n\n");
						break;
					case 1:
						arg0.setVariable("estadoPago", "Completo");
						conn.moverAPago(solicitudId, documento);
						System.out.print("\n\nINGRESO AL CASE DE ELSE IF 1\n\n");
						break;
					}
					System.out.print("\n\n\n EL VALOR DEL PAGO REALIZADO ES => " + valorPagoRealizado + "y el valor a pagar es " + valorAPagar + "y el estadoPago es " + estadoPago + "\n\n\n");
				}
			}
		} else {
			System.out.print("\n\nEN EL ELSE\n\n");
			arg0.setVariable("estadoPago", "No realizado");
		}
		
	}

}
 