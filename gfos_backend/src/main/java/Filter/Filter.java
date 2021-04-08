/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Filter;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author simon
 */
public class Filter{

    String path = "/swearwords.txt";
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(path).getFile());

    public Filter(){

    }

    public String filter(String s) throws IOException{
        Grawlox grawlox = Grawlox.createFromDefault(this.file);
        return grawlox.filter(s);
    }

    public boolean isProfane(String s) throws IOException{
        Grawlox grawlox = Grawlox.createFromDefault(this.file);
        return grawlox.isProfane(s);
    }
}
