package org.example.model;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PeliculaTableModel extends AbstractTableModel {
    private final List<Pelicula> data;
    private final String[] cols = {"Título","Año","Director"};

    public PeliculaTableModel(List<Pelicula> data){ this.data = data; }

    @Override public int getRowCount(){ return data.size(); }
    @Override public int getColumnCount(){ return cols.length; }
    @Override public String getColumnName(int c){ return cols[c]; }

    @Override public Object getValueAt(int r,int c){
        Pelicula p = data.get(r);
        return switch(c){
            case 0 -> p.getTitle();
            case 1 -> p.getYear();
            case 2 -> p.getDirector();
            default -> "";
        };
    }

    public Pelicula getAt(int row){ return data.get(row); }
    public void add(Pelicula p){ data.add(p); fireTableRowsInserted(data.size()-1, data.size()-1); }
    public void removeAt(int row){ data.remove(row); fireTableRowsDeleted(row, row); }
}
