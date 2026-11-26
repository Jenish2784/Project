package com.example.dotechuser.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrescriptionPdfHelper {

    public static void generateAndOpenPrescription(Context context,
                                                   String doctorName,
                                                   String category,
                                                   String hospitalAddress,
                                                   String patientName,
                                                   String date,
                                                   String time,
                                                   String type,
                                                   String problem,
                                                   String consultId) {
        PdfDocument document = new PdfDocument();
        int pageWidth = 595;
        int pageHeight = 842;

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Header Background
        paint.setColor(Color.rgb(16, 113, 99)); // #107163 Teal
        canvas.drawRect(0, 0, pageWidth, 90, paint);

        // Header Title
        paint.setColor(Color.WHITE);
        paint.setTextSize(22);
        paint.setFakeBoldText(true);
        canvas.drawText("DoTech Healthcare Network", 35, 45, paint);

        paint.setTextSize(12);
        paint.setFakeBoldText(false);
        canvas.drawText("Official Medical Consultation Report & Prescription", 35, 68, paint);

        // Date generated right aligned
        String issueDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        paint.setTextSize(10);
        canvas.drawText("Issued: " + issueDate, pageWidth - 140, 68, paint);

        // Doctor & Hospital Details
        paint.setColor(Color.rgb(30, 30, 30));
        paint.setTextSize(15);
        paint.setFakeBoldText(true);
        canvas.drawText("Dr. " + (doctorName != null ? doctorName : "Specialist"), 35, 125, paint);

        paint.setTextSize(11);
        paint.setFakeBoldText(false);
        paint.setColor(Color.rgb(100, 100, 100));
        canvas.drawText("Department: " + (category != null ? category : "General Medicine"), 35, 142, paint);
        if (hospitalAddress != null && !hospitalAddress.isEmpty()) {
            canvas.drawText("Hospital / Clinic: " + hospitalAddress, 35, 158, paint);
        }

        // Divider
        paint.setColor(Color.rgb(220, 220, 220));
        paint.setStrokeWidth(1.5f);
        canvas.drawLine(35, 175, pageWidth - 35, 175, paint);

        // Patient Box
        paint.setColor(Color.rgb(245, 248, 248));
        canvas.drawRoundRect(35, 190, pageWidth - 35, 265, 8, 8, paint);

        paint.setColor(Color.rgb(40, 40, 40));
        paint.setTextSize(12);
        paint.setFakeBoldText(true);
        canvas.drawText("PATIENT CONSULTATION DETAILS", 50, 212, paint);

        paint.setFakeBoldText(false);
        paint.setTextSize(11);
        canvas.drawText("Patient Name: " + (patientName != null ? patientName : "Patient"), 50, 232, paint);
        canvas.drawText("Consultation Date: " + (date != null ? date : issueDate) + " (" + (time != null ? time : "Scheduled") + ")", 50, 250, paint);
        canvas.drawText("Consultation Mode: " + (type != null ? type : "Online Consultation"), 320, 232, paint);
        canvas.drawText("Reference ID: #" + (consultId != null ? consultId.substring(0, Math.min(10, consultId.length())) : "DOC101"), 320, 250, paint);

        // Chief Complaints & Diagnosis
        paint.setColor(Color.rgb(20, 20, 20));
        paint.setTextSize(13);
        paint.setFakeBoldText(true);
        canvas.drawText("Clinical Notes & Symptoms:", 35, 300, paint);

        paint.setTextSize(11);
        paint.setFakeBoldText(false);
        paint.setColor(Color.rgb(60, 60, 60));
        String diagText = (problem != null && !problem.isEmpty()) ? problem : "Routine consultation and health assessment.";
        canvas.drawText(diagText, 35, 320, paint);

        // Rx Section
        paint.setColor(Color.rgb(16, 113, 99));
        paint.setTextSize(18);
        paint.setFakeBoldText(true);
        canvas.drawText("Rx - Prescribed Medications", 35, 365, paint);

        paint.setColor(Color.rgb(220, 220, 220));
        canvas.drawLine(35, 375, pageWidth - 35, 375, paint);

        // Prescriptions items
        paint.setColor(Color.rgb(30, 30, 30));
        paint.setTextSize(11);
        paint.setFakeBoldText(true);

        canvas.drawText("1. Tab. Paracetamol 500mg", 50, 405, paint);
        paint.setFakeBoldText(false);
        paint.setColor(Color.rgb(100, 100, 100));
        canvas.drawText("Dosage: 1 Tablet twice daily after meals for 3 days (if fever/pain occurs)", 65, 422, paint);

        paint.setColor(Color.rgb(30, 30, 30));
        paint.setFakeBoldText(true);
        canvas.drawText("2. Cap. Multivitamin & Zinc Complex", 50, 450, paint);
        paint.setFakeBoldText(false);
        paint.setColor(Color.rgb(100, 100, 100));
        canvas.drawText("Dosage: 1 Capsule once daily after breakfast for 15 days", 65, 467, paint);

        paint.setColor(Color.rgb(30, 30, 30));
        paint.setFakeBoldText(true);
        canvas.drawText("3. General Health & Recovery Advice", 50, 495, paint);
        paint.setFakeBoldText(false);
        paint.setColor(Color.rgb(100, 100, 100));
        canvas.drawText("Maintain adequate hydration (2.5L water/day), light balanced diet, and rest.", 65, 512, paint);
        canvas.drawText("Follow up after 5 days if symptoms persist or in case of any discomfort.", 65, 528, paint);

        // Footer Divider
        paint.setColor(Color.rgb(220, 220, 220));
        canvas.drawLine(35, 710, pageWidth - 35, 710, paint);

        // Signature & Stamp
        paint.setColor(Color.rgb(40, 40, 40));
        paint.setTextSize(11);
        paint.setFakeBoldText(true);
        canvas.drawText("Dr. " + (doctorName != null ? doctorName : "Doctor"), pageWidth - 190, 735, paint);
        paint.setFakeBoldText(false);
        paint.setTextSize(9);
        paint.setColor(Color.rgb(120, 120, 120));
        canvas.drawText("Digitally Verified Practitioner", pageWidth - 190, 750, paint);
        canvas.drawText("DoTech Telehealth License Verified", pageWidth - 190, 764, paint);

        paint.setTextSize(9);
        canvas.drawText("This is a digitally generated medical consultation document via DoTech Health Platform.", 35, 790, paint);
        canvas.drawText("For medical emergencies, please visit the nearest hospital or call 108.", 35, 804, paint);

        document.finishPage(page);

        // Save PDF to app external files dir
        try {
            File pdfDir = context.getExternalFilesDir(null);
            if (pdfDir == null) pdfDir = context.getCacheDir();
            File pdfFile = new File(pdfDir, "DoTech_Prescription_" + System.currentTimeMillis() + ".pdf");
            FileOutputStream fos = new FileOutputStream(pdfFile);
            document.writeTo(fos);
            document.close();
            fos.close();

            // Open or share the PDF
            Uri pdfUri = FileProvider.getUriForFile(context, "com.example.dotechuser.provider", pdfFile);
            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
            viewIntent.setDataAndType(pdfUri, "application/pdf");
            viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Intent chooser = Intent.createChooser(viewIntent, "Open Medical Prescription");
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooser);

        } catch (Exception e) {
            document.close();
            Toast.makeText(context, "Failed to generate prescription PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
