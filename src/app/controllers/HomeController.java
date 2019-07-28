package controllers;

import play.mvc.*;
import models.*;
import java.util.*;

public class HomeController extends Controller {

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result gerarDiagrama() {
        Map<String, String[]> r =
          request().body().asFormUrlEncoded();
        String formato = r.get("formato")[0];
        String tipo = r.get("tipo")[0];
        String cor = r.get("cor")[0];

        int contadorAtor = Integer.parseInt(r.get("contadorAtores")[0]);

        System.out.println("\nAtores = "+contadorAtor);

        boolean cor_;
        if(cor.equals("0")){
            cor_ = false;
        }else{
            cor_ = true;
        }

        String[] cores = {"blue","tomato","yellowgreen","turquoise","tan","wheat","yellow","whitesmoke"
        ,"seagreen","royalblue","orangered","mediumvioletred","mediumslateblue","lightslategray","lightsalmon","lightcoral","blue","darkgoldenrod","cadetblue","darkorange"};
        
        String url = "http://yuml.me/diagram/"+formato+"/usecase/";

        for(int j = 1; j <= contadorAtor; j++){
            // As variaveis ator e casos estao sendo geradas aqui pois caso haja mais de um ator, elas conseguirão receber multiplos valores (de acordo com a quantidade da var contadorAtor e a iteracao).
            String ator = r.get("ator"+j)[0];
            String[] casos = r.get("casos"+j)[0].split("\\r?\\n");

            for(int i = 0; i < casos.length; i++){
                int corAleatoria = new Random().nextInt(cores.length);

                String casoDeUso = "";
                int indexTagCorPersonalizada = 0; 

    
                if(cor_ == true){
                    boolean temCor = false;
                    for (int k = 0; k < casos[i].length(); k++) { 
                        System.out.print(casos[i].charAt(k));   
                        if(casos[i].charAt(k) == '#'){
                            temCor = true;
                            indexTagCorPersonalizada = k;
                        }
                    }
                    if(temCor){
                        casoDeUso = casos[i].substring(0, indexTagCorPersonalizada);
                        url += "["+ator+"]-("+casoDeUso+")"+"{bg:"+cores[corAleatoria]+"})";    

                    }else{
                        url += "["+ator+"]-("+casos[i]+")"+"{bg:"+cores[corAleatoria]+"})";    
                    }
                }else{
                    boolean temCor = false;
        
                    for (int k = 0; k < casos[i].length(); k++) { 
                        System.out.print(casos[i].charAt(k));   
                        if(casos[i].charAt(k) == '#'){
                            temCor = true;
                            indexTagCorPersonalizada = k;
                        }
                    }

                    casoDeUso = casos[i].substring(0, indexTagCorPersonalizada);
                    String corPersonalizada = casos[i].substring(indexTagCorPersonalizada+1, casos[i].length() );
                    
                    if(temCor){
                        url += "["+ator+"]-("+casoDeUso+")"+"{bg:"+corPersonalizada+"})";
                        System.out.println(" {TEM COR }= "+corPersonalizada);
                    }else{
                        url += "["+ator+"]-("+casos[i]+")"+"{bg:})";    
                        System.out.println(" {NAO TEM COR}");

                    }
                    
                }
                if(i != casos.length -1){
                    url += ",";
                }
            }
            url += ",";
        }

        if(tipo.equals(".pdf")){
            return redirect(url+tipo);
        }
        
        return ok(views.html.output.render(url+".png", url+tipo));//Para gerar um jpg tem que ter gerado um .png antes, por isso o primeiro parametro. Os demais formatos não são afetados.
    }

}
