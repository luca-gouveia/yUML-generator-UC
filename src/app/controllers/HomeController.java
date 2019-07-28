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

        System.out.println(contadorAtor);

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
            String ator = r.get("ator"+j)[0];
            String[] casos = r.get("casos"+j)[0].split("\\r?\\n");

            for(int i = 0; i < casos.length; i++){
                int corAleatoria = new Random().nextInt(cores.length);
    
                if(cor_ == true){
                    url += "["+ator+"]-("+casos[i]+")"+"{bg:"+cores[corAleatoria]+"})";    
                }else{
                    url += "["+ator+"]-("+casos[i]+")"+")";
                }
                if(i != casos.length -1){
                    url += ",";
                }
            }
        }

        System.out.println(url);

        if(tipo.equals(".pdf")){
            return redirect(url+tipo);
        }
        
        return ok(views.html.output.render(url+".png", url+tipo));//Para gerar um jpg tem que ter gerado um .png antes, por isso o primeiro parametro. Os demais formatos não são afetados.
    }

}
