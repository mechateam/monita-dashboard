package ta.simonita.monita.controller;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.userdetails.User;
import ta.simonita.monita.model.BalitaModel;
import ta.simonita.monita.model.PertumbuhanBalitaModel;
import ta.simonita.monita.model.UserModel;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheetIdentitas;
    private XSSFSheet sheetUkur;
    private List<UserModel> listUser;

    public ExcelExporter(List<UserModel> listUser) {
        this.listUser = listUser;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheetIdentitas = workbook.createSheet("Identitas");
        sheetUkur = workbook.createSheet("Ukur");

        Row row = sheetIdentitas.createRow(0);
        Row rowProb = sheetUkur.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "No", style);
        createCell(row, 1, "Anak ke", style);
        createCell(row, 2, "Tgl Lahir", style);
        createCell(row, 3, "Jenis Kelamin", style);
        createCell(row, 4, "Nama Anak", style);
        createCell(row, 5, "Berat Lahir (kg)", style);
        createCell(row, 6, "Tinggi (cm)", style);
        createCell(row, 7, "Nama Ortu", style);
        createCell(row, 8, "NIK Ortu", style);
        createCell(row, 9, "HP Ortu", style);
        createCell(row, 10, "Alamat", style);
        createCell(row, 11, "RT", style);
        createCell(row, 12, "RW", style);
        createCell(row, 13, "IMD", style);
        createCell(row, 14, "KIA", style);

        createCell(rowProb,0,"No",style);
        createCell(rowProb,1,"NIK",style);
        createCell(rowProb, 2, "Nama Anak", style);
        createCell(rowProb, 3, "Tanggal Ukur", style);
        createCell(rowProb, 4, "Berat (kg)", style);
        createCell(rowProb, 5, "Tinggi (cm)", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheetIdentitas.autoSizeColumn(columnCount);
        sheetUkur.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date){
            String pattern = "dd/MM/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            String todayAsString = df.format(value);
            cell.setCellValue(todayAsString);
        }else if (value instanceof Float){
            cell.setCellValue((Float) value);
        }else if (value instanceof Long){
            cell.setCellValue((Long) value);
        }
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        int rowPertumbuhan = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (UserModel user : listUser) {
            for (int i=0;i<user.getListBalita().size();i++){
                int columnCount = 0;
                Row row = sheetIdentitas.createRow(rowCount++);
                createCell(row, columnCount++, rowCount-1, style);
                createCell(row, columnCount++, i+1, style);
                createCell(row, columnCount++, user.getListBalita().get(i).getBirth_date(), style);
                createCell(row, columnCount++, user.getListBalita().get(i).getGender()==0 ? "Perempuan":"Laki-Laki", style);
                createCell(row, columnCount++, user.getListBalita().get(i).getName(), style);
                createCell(row, columnCount++, user.getListBalita().get(i).getBerat_badan_lahir(), style);
                createCell(row, columnCount++, user.getListBalita().get(i).getTinggi_badan_lahir(), style);
                createCell(row, columnCount++, user.getName(), style);
                createCell(row, columnCount++, user.getNik(), style);
                createCell(row, columnCount++, user.getPhone(), style);
                createCell(row, columnCount++, user.getAddress(), style);
                createCell(row, columnCount++, user.getRt(), style);
                createCell(row, columnCount++, user.getRw(), style);
                createCell(row, columnCount++, user.getListBalita().get(i).getImd()==false ? "Tidak":"Iya", style);
                createCell(row, columnCount++, user.getListBalita().get(i).getKia()==false ? "Tidak":"Iya", style);

                for (PertumbuhanBalitaModel pertumbuhan: user.getListBalita().get(i).getListPertumbuhan()){
                    Row rowProb = sheetUkur.createRow(rowPertumbuhan++);
                    int columnPertumbuhanCount = 0;

                    createCell(rowProb,columnPertumbuhanCount++,rowPertumbuhan-1,style);
                    createCell(rowProb,columnPertumbuhanCount++,user.getListBalita().get(i).getNik(),style);
                    createCell(rowProb,columnPertumbuhanCount++,user.getListBalita().get(i).getName(),style);
                    createCell(rowProb,columnPertumbuhanCount++,pertumbuhan.getInput_date(),style);
                    createCell(rowProb,columnPertumbuhanCount++,pertumbuhan.getBerat_badan(),style);
                    createCell(rowProb,columnPertumbuhanCount++,pertumbuhan.getTinggi_badan(),style);
                }
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}


