package Vista;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import Modelo.SQL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequestMapping("/login")
public class Login {
	
	SQL sql = new SQL();
	
	@GetMapping
	private ModelAndView Principal() {
		return new ModelAndView("login");
	}
	
	public void abrirNavegador(String url) {
		 try {
		        String os = System.getProperty("os.name").toLowerCase();
		        ProcessBuilder processBuilder;

		        if (os.contains("win")) {
		            // En Windows
		            processBuilder = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url);
		        } else if (os.contains("mac")) {
		            // En MacOS
		            processBuilder = new ProcessBuilder("open", url);
		        } else if (os.contains("nix") || os.contains("nux")) {
		            // En Linux
		            processBuilder = new ProcessBuilder("xdg-open", url);
		        } else {
		            throw new UnsupportedOperationException("Unsupported OS");
		        }

		        processBuilder.start();  // Inicia el proceso para abrir el navegador
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
    }
	
	@PostMapping
	private ModelAndView InciarSesion(@RequestParam String Usuario, @RequestParam String Contraseña) {
		
		String lector = "usuarios/";
		File carpeta = new File(lector);
		File[] archivos = carpeta.listFiles();
		String usuario = "";
		String contraseña = "";
		String correo = "";
		
		for(File archivo : archivos) {
			
			if(archivo.isFile() && archivo.getName().equals(Usuario + ".txt")){
				try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
					
                    String linea;
                    
                    while ((linea = br.readLine()) != null) {
                    	
                        if (usuario.isEmpty()) {
                        	
                            usuario = linea.trim(); // Primera línea: Usuario
                            
                        } else if (contraseña.isEmpty()) {
                        	
                            contraseña = linea.trim(); // Segunda línea: Contraseña
                            
                        } else if (correo.isEmpty()) {
                        	
                            correo = linea.trim(); // Tercera línea: Correo
                            
                        }
                    }
                    
                    if (Usuario.equals(usuario) && Contraseña.equals(contraseña)) {
                            	
                                System.out.println("Inicio de sesión exitoso para: " + Usuario);
                                sql.setUsuario(usuario);
                                sql.setContraseña(contraseña);
                                sql.setCorreo(correo);
                                sql.setUser(sql);
                                return new ModelAndView("/panel");
                                
                    }
                    
                } catch (IOException e) {
                    System.out.println("Error al leer el archivo: " + e.getMessage());
                }
			}
				
		}
		
		return new ModelAndView("/login");
	}
	
}
