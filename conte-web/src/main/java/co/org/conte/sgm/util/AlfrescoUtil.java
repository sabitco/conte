package co.org.conte.sgm.util;

import co.org.conte.sgm.dao.ParametroDao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author J4M0
 */
public class AlfrescoUtil extends HttpServlet{
    
    /**
     * Se conecta a Alfresco con la IP que le sea indicada
     * @param ip
     * @return 
     */
    public static String getTicket(){
        try {
            ParametroDao dao = new ParametroDao();
            String ip = dao.getParameter("ip");
            String user = dao.getParameter("user");
            String password = dao.getParameter("password");   
            URL url = new URL(ip + "alfresco/service/api/login?u="+user+"&pw="+password);      
             
            System.out.print("\n\n\n\n\n\n\n\n"+url+"\n\n\n\n\n\n\n\n\n\n");
            BufferedReader in = null;            
            in = new BufferedReader(new InputStreamReader(
                    url.openStream()));
                        
            String inputLine;
            String inputText="";
            
            while ((inputLine = in.readLine()) != null){
                inputText = inputText + inputLine;
            }
            in.close();
		   
            Pattern pattern = Pattern.compile(".*[<][t][i][c][k][e][t][>](.*)[<][/][t][i][c][k][e][t][>].*");
            Matcher matcher = pattern.matcher(inputText);
            if( matcher.matches() ){			
                System.out.println(matcher.group(1));
                return matcher.group(1);
            }		
             
        } catch (MalformedURLException me) {
            me.printStackTrace();
            System.out.println("URL erronea");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Error IO");
        }
        
        return null;
    }    
}