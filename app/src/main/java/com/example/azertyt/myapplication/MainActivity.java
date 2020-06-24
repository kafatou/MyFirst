package com.example.azertyt.myapplication;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Button precedent,valider,suivant;
    private MainActivity activity;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    ArFragment arFragment;
    private static final ArrayList<Plat>Commande=new ArrayList<Plat>();
    ModelRenderable lampPostRenderable;

    private static final ArrayList<Plat>liste_plat=new ArrayList<Plat>();
    private static  final HashMap<String,String> listeChemin=new HashMap<>();
    private static String chemin="model.sfb";
    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity=this;
        precedent=findViewById(R.id.pcdt);
        valider=findViewById(R.id.vld);
        suivant=findViewById(R.id.svt);
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        ModelRenderable.builder()
                .setSource(this, Uri.parse(chemin))
                .build()
                .thenAccept(renderable -> lampPostRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
        arFragment.setOnTapArPlaneListener(
                (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
                    if (lampPostRenderable == null){
                        return;
                    }
                    Anchor anchor = hitresult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    TransformableNode lamp = new TransformableNode(arFragment.getTransformationSystem());
                    lamp.setParent(anchorNode);
                    lamp.setRenderable(lampPostRenderable);
                    lamp.select();
                }
        );
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder myCommand=new AlertDialog.Builder(activity);

                TextView textView1= new TextView(MainActivity.this);
                textView1.setText("donner le nombre de plats");
                EditText editNbre=new EditText(MainActivity.this);
                TextView textView2= new TextView(MainActivity.this);
                textView1.setText("donner la taille");
                EditText editTaille=new EditText(MainActivity.this);
                LinearLayout layout=new LinearLayout(MainActivity.this);
                layout.addView(textView1);
                layout.addView(editNbre);
                layout.addView(textView2);
                layout.addView(editTaille);

                myCommand.setTitle("Détails de la commande");
                myCommand.setView(layout);
                myCommand.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        int nbre=Integer.valueOf(editNbre.getText().toString());
                        String taille=editTaille.getText().toString();

                        String valeur=listeChemin.get(chemin);
                        Plat plat=getPlat(valeur);
                        plat.setNbrePlat(nbre);
                        plat.setTaille(taille);

                        Commande.add(plat);
                    }
                });
                myCommand.setNegativeButton("Annuler",null);
                AlertDialog dialog=myCommand.create();
                dialog.show();
            }
        });
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Set<Map.Entry<String, String>> entrees = listeChemin.entrySet ( ) ;
                Iterator <Map.Entry<String, String>> iter = entrees.iterator ( ) ;
                while(iter.hasNext())
                {
                    Map.Entry<String, String> entree =  iter.next ( ) ;
                    chemin=entree.getKey();
                }
                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        precedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Set<Map.Entry<String, String>> entrees = listeChemin.entrySet ( ) ;
                Iterator <Map.Entry<String, String>> iter = entrees.iterator ( ) ;
                while(iter.hasPrevious())
                {
                    Map.Entry<String, String> entree =  iter.previous() ;
                    chemin=entree.getKey();
                }
                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        });
    }


    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    public static final void AjoutPlat()
    {
        Plat p1=new Plat("idNouille","nouille",0,null);
        Plat p2=new Plat("idPizza","pizza",0,null);

        liste_plat.add(p1);
        liste_plat.add(p2);

        listeChemin.put("model.sfb",p1.getIdPlat());
        listeChemin.put("pizza.sfb",p2.getIdPlat());
    }

    public static final Plat getPlat(String idPlat )
    {
        Plat plat=null;
        Iterator iterator= liste_plat.iterator();
        while(iterator.hasNext())
        {
            Plat plat1=(Plat)iterator.next();
            if(plat1.getIdPlat().equals(idPlat))
                plat=plat1;
        }
        return plat;
    }
}