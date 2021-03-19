/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;
import java.util.Comparator;
import Entity.Chat;

/**
 *
 * @author simon
 */
public class DateSorter implements Comparator<Chat> {
    
    @Override
    public int compare(Chat c1, Chat c2){
        return c2.getLetztenachricht().getDatumuhrzeit().compareTo(c1.getLetztenachricht().getDatumuhrzeit());
    }
}
