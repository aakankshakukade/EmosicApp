package com.example.gauri.projectsem;

import android.app.Activity;
import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.provider.MediaStore;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AlertDialog;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.view.*;

        import com.microsoft.projectoxford.emotion.EmotionServiceClient;
        import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
        import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
        import com.microsoft.projectoxford.emotion.contract.Scores;

        import java.io.ByteArrayInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.List;

public class MainActivity extends Activity {
    double maxNum; String status;
    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap mBitmap=null;
    ImageView imageView;
    public EmotionServiceClient emotionServiceClient = new EmotionServiceRestClient("45e117c14ad040d49296d81afe4ead5b");
    ByteArrayInputStream inputStream = null;
   private ImageButton ib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ib= (ImageButton) findViewById(R.id.imageButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Mood.class);
                startActivity(intent);
            }
        });
    }

//   public void selectmood(View v)
//   {
//       Intent moodintent = new Intent(MainActivity.this,Mood.class);
//       startActivity(moodintent);
//   }
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         imageView= (ImageView) findViewById(R.id.imageview);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(mBitmap);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        AsyncTask<InputStream, String, List<RecognizeResult>> emotionTask = new AsyncTask<InputStream, String, List<RecognizeResult>>() {
            ProgressDialog mDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected List<RecognizeResult> doInBackground(InputStream... params) {
                try {
                    publishProgress("Recognizing....... ");
                    List<RecognizeResult> result = emotionServiceClient.recognizeImage(params[0]);
                    return result;

                } catch (Exception ex) {
                    return null;

                }
            }

            @Override
            protected void onPreExecute() {
                mDialog.show();

            }

            @Override
            protected void onPostExecute(List<RecognizeResult> recognizeResults) {
                mDialog.dismiss();
                for (RecognizeResult res : recognizeResults) {
                     status = getEmo(res);
                    imageView.setImageBitmap(ImageHelper.drawRectOnBitmap(mBitmap, res.faceRectangle, status));
                   // Toast.makeText(MainActivity.this, status, Toast.LENGTH_LONG).show();


                }
                if(status==null)
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog,null);
                    dialogBuilder.setView(dialogView);

                    final TextView Text = (TextView) dialogView.findViewById(R.id.Textview);
                    Text.setText("No Face detected!!");
                    dialogBuilder.setTitle("Emosic");
                    dialogBuilder.setMessage("");
                    dialogBuilder.setNegativeButton("Go Back",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton)
                        {

                        }
                    });
                    AlertDialog dig = dialogBuilder.create();
                    dig.show();
                }
                //=========================================================================
else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog, null);
                    dialogBuilder.setView(dialogView);

                    final TextView Text = (TextView) dialogView.findViewById(R.id.Textview);
                    Text.setText("Seems you are " + status);
                    dialogBuilder.setTitle("Emosic");
                    dialogBuilder.setMessage("");
                    dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent jump = new Intent(MainActivity.this, MusicPlayerB.class);
                            jump.putExtra("emotion",status);
                            startActivity(jump);
                        }
                    });
                    dialogBuilder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    AlertDialog dig = dialogBuilder.create();
                    dig.show();

                }
                //==========================================================================

            }

            @Override
            protected void onProgressUpdate(String... values) {
                mDialog.setMessage(values[0]);
            }
        };
        emotionTask.execute(inputStream);

    }



    private String getEmo(RecognizeResult res) {
        List<Double> list = new ArrayList<>();
        Scores scores = res.scores;
        list.add(scores.anger);
        list.add(scores.happiness);
        list.add(scores.contempt);
        list.add(scores.disgust);
        list.add(scores.fear);
        list.add(scores.neutral);
        list.add(scores.sadness);
        list.add(scores.surprise);
        Collections.sort(list);


        double maxNum = list.get(list.size() - 1);
        if (maxNum == scores.anger) {


            return "Anger";
        }

        else if (maxNum == scores.happiness)
            return "Happy";


        else if (maxNum == scores.contempt)



            return "Contempt";


        else if (maxNum == scores.disgust)
            return "Disgust";

        else if (maxNum == scores.fear)

            return "Fear";


        else if (maxNum == scores.neutral)


                return "Neutral";


        else if (maxNum == scores.sadness)


                return "Sad";


        else if (maxNum == scores.surprise)


                return "surprise";


        else

                return "Neutral";

    }
    @Override
        protected void onPause(){
            super.onPause();
            Intent i= new Intent(this,MainActivity.class);
        }


//if()
//
//
//    public void open(View view) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        dialogBuilder.setMessage("Is this the emotion you were looking for?");
//
//
//        dialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int yes) {
//                //opens media player
//                //sends emotion to emotion manager
//            }
//
//
//        });
//        dialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener(){
//            public void onClick(DialogInterface dialog, int no)
//            {
//                //redo
//            }
//        });
//        AlertDialog dig = dialogBuilder.create();
//        dig.show();
//    }

}