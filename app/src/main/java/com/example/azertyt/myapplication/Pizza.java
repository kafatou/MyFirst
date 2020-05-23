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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.azertyt.myapplication.MainActivity;
import com.example.azertyt.myapplication.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;

public class Pizza  extends AppCompatActivity
{
        TextView precedent,valider,suivant;
        String item;
        private static final String TAG = com.example.azertyt.myapplication.MainActivity.class.getSimpleName();
        private static final double MIN_OPENGL_VERSION = 3.0;
        ArFragment arFragment;
        ModelRenderable lampPostRenderable;
        boolean b=false;
        ArrayList<Plat>Commande=new ArrayList<Plat>();
        @Override
        @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            precedent=findViewById(R.id.pcdt);
            valider=findViewById(R.id.vld);
            suivant=findViewById(R.id.svt);
            if (!checkIsSupportedDeviceOrFinish(this)) {
                return;
            }
            setContentView(R.layout.activity_main);
            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
            ModelRenderable.builder()
                    .setSource(this, Uri.parse("pizza.sfb"))
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
            String inputData=this.getIntent().getExtras().getString("Commande");
            valider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder myCommand=new AlertDialog.Builder(Pizza.this);
                    View mView=getLayoutInflater().inflate(R.layout.spinner_dialog,null);
                    myCommand.setTitle("Détails de la commande");
                    myCommand.setMessage("Combien vous en voulez??");

                    Spinner mSpinner=(Spinner) mView.findViewById(R.id.spin);
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(Pizza.this,
                            android.R.layout.simple_spinner_item,
                            getResources().getStringArray(R.array.list));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner.setAdapter(adapter);
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            item=adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    myCommand.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            Plat P1=new Plat(item,null);
                            Commande.add(P1);
                        }
                    });
                    myCommand.setNegativeButton("Annuler",null);
                    myCommand.setView(mView);
                    AlertDialog dialog=myCommand.create();
                    dialog.show();
                }
            });
            precedent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                    if (!Commande.isEmpty())
                    {
                        intent.putExtra("Commande",Commande);
                    }
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
}
