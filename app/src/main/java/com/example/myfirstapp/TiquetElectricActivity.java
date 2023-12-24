package com.example.myfirstapp;

import static com.itextpdf.svg.converter.SvgConverter.createPdf;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.os.Environment;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TiquetElectricActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iconMenu;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnTiquet;
   private int i=0,iA=0,iB=0,iC=0,iActuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiquet_electric);

        drawerLayout=findViewById(R.id.drawer_layout_tiquet);
        navigationView=findViewById(R.id.navigation_view_tiquet);
        iconMenu=findViewById(R.id.icon_tiquet);
        radioGroup=findViewById(R.id.rgTiquet);
        btnTiquet=findViewById(R.id.idButtonTiquet);

        btnTiquet.setOnClickListener(view -> {
            int radioId=radioGroup.getCheckedRadioButtonId();
            radioButton=findViewById(radioId);
            String nameTiquet=radioButton.getText().toString();

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    createPdf(nameTiquet);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });


        navigationDrawer();

        navigationView.setNavigationItemSelectedListener( item -> {
            if(item.getItemId()==R.id.devices){
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(TiquetElectricActivity.this,HomeActivity.class));

            }
            if (item.getItemId()==R.id.tiquetsElectriques) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            if (item.getItemId()==R.id.profile) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(TiquetElectricActivity.this,ProfilActivity.class));
            }
            return true;
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createPdf(String nameTicket) throws FileNotFoundException{
        i++;

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath , "TicketElectrique"+i+".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A6);
        document.setMargins(5,5,5,5);

        Drawable d = getDrawable(R.drawable.img_1);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        image.setWidth(100).setHeight(100).setHorizontalAlignment(HorizontalAlignment.CENTER);

        Paragraph title = new Paragraph("Ticket Electrique").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER);
        Paragraph welcome = new Paragraph("Welcome").setBold().setFontSize(15).setTextAlignment(TextAlignment.CENTER);

        Paragraph nameTicketElectrique = null,numTicket=null;
        if (nameTicket.equals("Choix A")){
            iA++;
            nameTicketElectrique = new Paragraph("Choix A").setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER);
            numTicket = new Paragraph("A0"+iA).setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER);
            iActuel=iA;
        }else if (nameTicket.equals("Choix B")){
            iB++;
            nameTicketElectrique = new Paragraph("Choix B").setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(12);
            numTicket = new Paragraph("B0"+iB).setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(14);
            iActuel=iB;
        }else if (nameTicket.equals("Choix C")){
            iC++;
            nameTicketElectrique = new Paragraph("Choix C").setFontSize(12).setBold().setTextAlignment(TextAlignment.CENTER);
            numTicket = new Paragraph("C0"+iC).setFontSize(14).setBold().setTextAlignment(TextAlignment.CENTER);
            iActuel=iC;
        }

        float[] width = {100f,100f};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        DateTimeFormatter dateTimeFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        table.addCell(new Cell().add(new Paragraph("Date")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            table.addCell(new Cell().add(new Paragraph(LocalDate.now().format(dateTimeFormatter).toString())));
        }

        DateTimeFormatter timeFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        }
        table.addCell(new Cell().add(new Paragraph("Temps")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            table.addCell(new Cell().add(new Paragraph(LocalTime.now().format(timeFormatter).toString())));
        }

        table.addCell(new Cell().add(new Paragraph("Numero Actuel")));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(iActuel))));


        document.add(image);
       document.add(title);
       document.add(welcome);
       document.add(nameTicketElectrique);
       document.add(numTicket);
        document.add(table);

        document.close();

        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();


    }


    private void navigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.tiquetsElectriques);
        navigationView.bringToFront();

        iconMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorAPP));
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;

    }


    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();

    }
    }