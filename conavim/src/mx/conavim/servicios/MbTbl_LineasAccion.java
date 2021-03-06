package mx.conavim.servicios;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;

import mx.conavim.control.TblActividadDAO;
import mx.conavim.control.Tbl_lineasaccionDAO;
import mx.conavim.modelo.TblActividad;
import mx.conavim.modelo.TblRespuesta;
import mx.conavim.modelo.Tbl_Estrategia;
import mx.conavim.modelo.Tbl_Informes;
import mx.conavim.modelo.tbl_lineasaccion;

public class MbTbl_LineasAccion {

	Tbl_lineasaccionDAO consultas;
	private List<tbl_lineasaccion> lineasaccion = new ArrayList<tbl_lineasaccion>();
	private List<tbl_lineasaccion> tempLineas = new ArrayList<tbl_lineasaccion>();
	private List<Tbl_Estrategia>  estrategias = new ArrayList<Tbl_Estrategia>();
	private List<Tbl_Estrategia>  tempEstrategias = new ArrayList<Tbl_Estrategia>();	
	private List<String> productos = new ArrayList<String>();
	private List<String> actividades = new ArrayList<String>();
	
	private int estrategia=1;	
	private int objetivo=1;
	private int idLinea=1;
	private String idInforme;
	TblRespuesta otblRespuesta;
	private int iteamActivo=0;
	private String mensaje="";
	private Tbl_Informes selInforme;
	private TblActividadDAO oTblActividadDAO;
	private UploadedFile file;
	private String rutaImagen="";
	private String nombreImagen="";
	
	String uploadFileLocation;
	
	
	
	public String getNombreImagen() {
		return nombreImagen;
	}

	public void setNombreImagen(String nombreImagen) {				
		this.nombreImagen = nombreImagen;
	}
	private String extension;
	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public void armarImagen(String imagen){		
		rutaImagen="/programaintegral/images/"+selInforme.getId_informe()+"/"+idLinea+"/"+imagen;
		extension=rutaImagen.substring(rutaImagen.length()-3, rutaImagen.length());
		System.out.println("Armando ruta-->"+rutaImagen+"\nExtension->"+extension);
		
//		rutaImagen="/opt/shared/glassfish/domains/domain1/img/"+selInforme.getId_informe()+"/"+idLinea+"/"+imagen;
		
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public Tbl_Informes getSelInforme() {
		return selInforme;
	}

	public List<String> getListFiles() {
		return listFiles;
	}

	public void setListFiles(List<String> listFiles) {
		this.listFiles = listFiles;
	}

	public void setSelInforme(Tbl_Informes selInforme) {
		//System.out.println("nombre_entidad->"+selInforme.getNombreEntidad());
		this.selInforme = selInforme;
		cargarDatos();
		obtenerImages();
	}

	public int getIteamActivo() {
		return iteamActivo;
	}

	public void setIteamActivo(int iteamActivo) {
		this.iteamActivo = iteamActivo;
	}
	private boolean pintarBotones=true;
	
	
	
	
	public boolean isPintarBotones() {
		return pintarBotones;
	}

	public void setPintarBotones(boolean pintarBotones) {
		this.pintarBotones = pintarBotones;
	}

	public String getIdInforme() {
		return idInforme;
	}

	public void setIdInforme(String idInforme) {
		//System.out.println("Seteando id informe-->"+idInforme);
		this.idInforme = idInforme;
	}

	public TblRespuesta getOtblRespuesta() {
		return otblRespuesta;
	}

	public void setOtblRespuesta(TblRespuesta otblRespuesta) {
		this.otblRespuesta = otblRespuesta;
	}

	public int getEstrategia() {
		return estrategia;
	}

	public int getIdLinea() {
		return idLinea;
	}

	public void setIdLinea(int idLinea) {
		this.idLinea = idLinea;
	}

	public int getObjetivo() {
		return objetivo;
	}


	public void setObjetivo(int objetivo) {
		this.objetivo = objetivo;
	}

	public List<String> getProductos() {
		return productos;
	}



	public void setProductos(List<String> productos) {
		//inicializar();
		this.productos = productos;
	}



	public List<tbl_lineasaccion> getLineasaccion() {
		return lineasaccion;
	}



	public void setLineasaccion(List<tbl_lineasaccion> lineasaccion) {
		this.lineasaccion = lineasaccion;
	}
	
	

	public List<String> getActividades() {
		return actividades;
	}

	public void setActividades(List<String> actividades) {
		this.actividades = actividades;
	}

	
	
	public void cargarDatos(){
		objetivo = 1;
		estrategia=1;
		idLinea=1;
		iteamActivo=0;
		
		if(otblRespuesta ==null){
			//System.out.println("inicializando TblRespuesta");
			otblRespuesta=new TblRespuesta();
		}
		 //Obtenemos idInforme
//		 FacesContext fc = FacesContext.getCurrentInstance();
//	     Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
//	     this.idInforme =  params.get("idInforme");
//	     System.out.println("Metodo init-->");
	     System.out.println("id informe en cargar datos-->"+selInforme.getId_informe());
		consultas=new Tbl_lineasaccionDAO();
		tempLineas=consultas.getLineasAccion();
		productos = consultas.getProductos();
		oTblActividadDAO = new TblActividadDAO();
		actividades = oTblActividadDAO.getAllActividades();
		tempEstrategias = consultas.getEstrategias();
		llenarEstrategias(1);
		//verificando si existe linea con id informe
		otblRespuesta = consultas.verificarExisteLinea(selInforme.getId_informe(),idLinea);
		validarBotones(otblRespuesta);
		//System.out.println("Valor de id Informe-->"+idInforme);
		consultas.dispose();
	}

//	@PostConstruct
//	public void init() {
//		if(otblRespuesta ==null){
//			//System.out.println("inicializando TblRespuesta");
//			otblRespuesta=new TblRespuesta();
//		}
//		 //Obtenemos idInforme
//		 FacesContext fc = FacesContext.getCurrentInstance();
//	     Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
//	     this.idInforme =  params.get("idInforme");
//	     System.out.println("Metodo init-->");
//	     System.out.println("id informe-->"+idInforme);
//		consultas=new Tbl_lineasaccionDAO();
//		tempLineas=consultas.getLineasAccion();
//		productos = consultas.getProductos();
//		tempEstrategias = consultas.getEstrategias();
//		llenarEstrategias(1);
//		//verificando si existe linea con id informe
//		otblRespuesta = consultas.verificarExisteLinea(idInforme,idLinea);
//		validarBotones(otblRespuesta);
//		//System.out.println("Valor de id Informe-->"+idInforme);
//		consultas.dispose();
//	}
	
	
	public List<Tbl_Estrategia> getEstrategias() {
		return estrategias;
	}



	public void setEstrategias(List<Tbl_Estrategia> estrategias) {
		this.estrategias = estrategias;
	}



	public void llenarLineas(int estrategia){
	//	System.out.println("ejecutando metodo llenar lineas para estrategia-->"+estrategia);
			lineasaccion.clear();
			for(tbl_lineasaccion val:tempLineas){								
				if(val.getId_estrategia() == estrategia){
					//System.out.println("sacando nuevas listas----->"+val.getId_estrategia()+"--nombre-->"+val.getDescripcion());
					lineasaccion.add(val);
				}				
			}
			this.idLinea = lineasaccion.get(0).getId_lineaaccion();
			//System.out.println("id Linea accion->"+idLinea);
//			for(tbl_lineasaccion val:lineasaccion){
//				System.out.println("sacando nuevas Estrategi----->"+val.getId_estrategia()+"--nombre-->"+val.getNombre_linea()+"----descripcion---->"+val.getDescripcion());
//			}
	}
	
	public void llenarEstrategias(int objetivo){
		//System.out.println("ejecutando metodo llenar estrategias para objetivo-->"+objetivo);
			estrategias.clear();
			for(Tbl_Estrategia val:tempEstrategias){
				if(val.getId_objetivo() == objetivo){
					//System.out.println("sacando nuevas listas----->"+val.getId_estrategia()+"--nombre-->"+val.getDescripcion());
					estrategias.add(val);					
				}				
			}			
			int ob = estrategias.get(0).getId_estrategia();
			this.estrategia = ob;
			//System.out.println("id Estrategia->"+estrategia);
			llenarLineas(ob);			
//			for(tbl_lineasaccion val:lineasaccion){
//				System.out.println("sacando nuevas listas Estrategias----->"+val.getId_estrategia()+"--nombre-->"+val.getNombre_linea()+"--->descripcion"+val.getDescripcion());
//			}
	}
	
	public void setEstrategia(int estrategia) {
		this.estrategia = estrategia;
	}

	public void onTabChangeEstrategia(TabChangeEvent event) {		
        FacesMessage msg = new FacesMessage(event.getTab().getTitletip());        
        String est = (msg.getDetail());
        estrategia = Integer.parseInt(est);
        //System.out.println("id Estrategia-->"+estrategia);
        llenarLineas(estrategia);
        iteamActivo=0;
        consultas=new Tbl_lineasaccionDAO();
        otblRespuesta = consultas.verificarExisteLinea(selInforme.getId_informe(),idLinea);
		validarBotones(otblRespuesta);
		consultas.dispose();
		obtenerImages();
    }
	
	public void onTabChangeObjetivo(TabChangeEvent event) {		
        FacesMessage msg = new FacesMessage(event.getTab().getTitle());
        String est = (msg.getDetail()).replace("Objetivo ", "");
        objetivo = Integer.parseInt(est);
        //System.out.println("id Objetivo-->"+objetivo);
        llenarEstrategias(objetivo);  
        iteamActivo=0;
        consultas=new Tbl_lineasaccionDAO();
        otblRespuesta = consultas.verificarExisteLinea(selInforme.getId_informe(),idLinea);
		validarBotones(otblRespuesta);
		consultas.dispose();
		obtenerImages();
        //System.out.println("Linea-->"+idLinea);
    }
	
	public void onTabChangeLinea(TabChangeEvent event) {		
        FacesMessage msg = new FacesMessage(event.getTab().getTitletip());
        String est = (msg.getDetail());
        idLinea = Integer.parseInt(est);
        //System.out.println("id Linea-->"+idLinea);
        
        consultas=new Tbl_lineasaccionDAO();
        otblRespuesta = consultas.verificarExisteLinea(selInforme.getId_informe(),idLinea);
		validarBotones(otblRespuesta);
		consultas.dispose();
		obtenerImages();
    }

	public void insertarRespuesta(){		
		otblRespuesta.setId_lineaaccion(idLinea);		
		//llevar este obtjeto para insercion		
	}
	
	 public void guardarLinea(String clave) throws ParseException {
		 
		 //otblRespuesta=new TblRespuesta();		
	     //Realizamos la consulta
		 consultas=new Tbl_lineasaccionDAO();
		 //System.out.println("INSERTANDO REGISTRO");
		 //otblRespuesta.setFechainactv(validarFecha(otblRespuesta.getFechainactv()));
		 //otblRespuesta.setFechatermactv(validarFecha(otblRespuesta.getFechatermactv()));		 
		 otblRespuesta.setId_lineaaccion(idLinea);
		 otblRespuesta.setId_informe(selInforme.getId_informe());		 
		 //System.out.println("Valor de informe-->"+idInforme);
		 //System.out.println("valor pintarBotones:->"+pintarBotones);
		 if(pintarBotones){
			 if(file!=null)
			 		upload(file);
			 
		 	consultas.insertarRespuestas(otblRespuesta);
		 	mensaje=consultas.getMensaje();	
		 	notificacionMessage();
		 	pintarBotones=false;
		 	
		 }else{	
			 if(file!=null)
			 		upload(file);
			 		
			 otblRespuesta=consultas.actualizarRespuestas(otblRespuesta, selInforme.getId_informe(), idLinea);
			 mensaje=consultas.getMensaje();
			 notificacionMessage();			 
		 }
		 consultas.dispose();		 	     
		 
	    }
	 
	 public java.sql.Date validarFecha(TblRespuesta oTblRespuesta,Date fecha) throws ParseException{
		 java.sql.Date fechaValidada=null;
		 if(fecha!=null){
		 	SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
			String fechaOrdenada=formateador.format(fecha);
			fechaValidada = java.sql.Date.valueOf(fechaOrdenada);			
		 }
		 return fechaValidada;
	 }
	 
	 public void redirectIndex(String id_informe)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			try {				
				context.getExternalContext().redirect("index.conavim");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	 public void validarBotones(TblRespuesta otblRespuesta){
		 if(otblRespuesta.getId_informe()!=null){
			 pintarBotones=false;
			 //System.out.println("actualizar");
			 //mensaje=consultas.getMensaje();
			 
		 }else{
			 //System.out.println("Insertar");
			 pintarBotones=true;
			 //mensaje=consultas.getMensaje();
			 //mensaje=consultas.getMensaje();
		 }
	 }
	 
	 public void notificacionMessage() {
	        FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage("COMPLETADO",  "" + mensaje) );
	    }
	 
	 public void handleFileUpload(FileUploadEvent event) {
	        //upload(event.getFile());
		 	//this.file=event.getFile();
		 	upload(event.getFile());
		 	mensaje="Archivo Subido";
		 	notificacionMessage();
	        System.out.println("cachando archivo--->!!!");
	        event=null;
	        obtenerImages();
	 }	 	 
	 
	 private List<String> listFiles = new ArrayList<>();
	 
	 public void obtenerImages(){
	    	
	  		try {
	  			listFiles.clear();
	  			String path = this.getClass().getClassLoader().getResource("").getPath();				    	
	  			String fullPath = "/opt/shared/glassfish/domains/domain1/img/";
		  		String reponsePath = "";
		  		reponsePath = fullPath +selInforme.getId_informe()+"/"+idLinea+"/";		  		
	            File theDir = new File(reponsePath);
	            if (theDir.exists()){            	
	                File prueba= new File(reponsePath); 
	                File[] ficheros =prueba.listFiles(); 
	                File f=null; 
	                if(prueba.exists()){ 
	                	for (int x=0;x<ficheros.length;x++) { 
	                			f= new File(ficheros[x].toString());
	                			listFiles.add(f.getName().toString());
//	                			System.out.println(x+"-->"+ficheros[x].toString());                			
	                	}                  			                		               
	                }
	            }else{
	            	System.out.println("No exise archivos->"+reponsePath);
	            }
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	 


	public void upload(UploadedFile file) {
						
		      
			try
			{				
				      if(file != null) 
				      {			        	
			            //String subPath=("C:\\ficheros\\data\\archivos\\"+selInforme.getId_informe()+"\\"+idLinea+"\\");
//				    	String path = this.getClass().getClassLoader().getResource("").getPath();				    	
				  		String fullPath = "/opt/shared/glassfish/domains/domain1/img/";
				  		String reponsePath = "";
				  		reponsePath = fullPath +selInforme.getId_informe()+"/"+idLinea+"/";
				    	//String subPath=("/data/archivos/"+selInforme.getId_informe()+"/"+idLinea+"/");
				  		System.out.println("ruta para subir archivo: "+reponsePath);
		                

		                //String subPath=("/opt/shared/glassfish/domains/domain1/data/");
		                //System.out.println("subpath para guardar archivos de pregunta 6: " + subPath);
		                File theDir = new File(reponsePath);
		                if (!theDir.exists())
		                	theDir.mkdirs();
//		                else{
//		                	theDir.delete();			                
//			                File prueba= new File(reponsePath); 
//			                File[] ficheros =prueba.listFiles(); 
//			                File f=null; 
//			                if(prueba.exists()){ 
//			                	for (int x=0;x<ficheros.length;x++) { 
//			                			f= new File(ficheros[x].toString()); 
//			                			System.out.println(x+"-->"+ficheros[x].toString());
//			                			f.delete(); 
//			                	} 
//			                } 			                		                
//		                }
		                
		                if(file != null) 
					      {
				            InputStream uploadedInputStream = file.getInputstream();
					  		this.uploadFileLocation=reponsePath+ file.getFileName();
					  		OutputStream out = null;
					  		try{
					  		   int read=0;
					  		   byte[] bytes=new byte[1024];
					  	       out= new FileOutputStream(new File(uploadFileLocation));					  		   
					  		   while((read=uploadedInputStream.read(bytes))!=-1)
					  		    {
					  		      out.write(bytes, 0, read);
					  		    }
					  		   out.flush();
					  		   out.close();
					  		   out=null;
					  		   uploadedInputStream.close();
					  		   		System.out.println("Cargado correctamente!!:.");
					  		   		//mensaje+="<br/> Archivo cargado Correctamente!";
					  		   		//this.otblRespuesta.setLinkproducto(uploadFileLocation);	
					  		  }catch(IOException execption){
					  			  execption.printStackTrace(); 
					  		   		mensaje+="<br/> Error al cargar archivo";
					  		  }finally{
					  		   //out.close();					  		   
					  		   uploadedInputStream.close();
					  		   file=null;
					  		   this.file=null;
					  		   uploadedInputStream=null;
					  		   theDir=null;
					  		  }
				           
																							    					    
				        }
																						    					    
			        }
				    this.file=null;
				    uploadFileLocation=null;
				    file=null;
			}
			catch(Exception ex)
			{				 
				ex.printStackTrace();
			}
		}
	 

	 
	
}
