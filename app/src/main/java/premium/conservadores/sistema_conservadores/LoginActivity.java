package premium.conservadores.sistema_conservadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import premium.conservadores.sistema_conservadores.Clases.clsUsuario;

public class LoginActivity extends AppCompatActivity {

    private static final String url = "jdbc:mysql://162.241.62.225:3306/dmecommx_sistemapi?characterEncoding=latin1";
    private static final String user = "dmecommx_khaf";
    private static final String pass = "Resident123";
    private Button btnEntrar;
    SharedPreferences Configuraciones;
    SharedPreferences.Editor EditarConf;
    EditText txtUsuario, txtContrasena;
    clsUsuario datosus;
    ProgressBar pbCargar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Configuraciones = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EditarConf = Configuraciones.edit();
        btnEntrar = (Button) findViewById(R.id.btnIngresar);
        txtUsuario = (EditText) findViewById(R.id.txtUser);
        txtContrasena = (EditText) findViewById(R.id.txtPass);
        pbCargar = (ProgressBar) findViewById(R.id.pbCargarUser);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtContrasena.getText().toString().isEmpty() && !txtUsuario.getText().toString().isEmpty()) {
                    ValidaUsuarioMySql validar = new ValidaUsuarioMySql();
                    validar.execute();
                }else
                    Toast.makeText(getApplicationContext(), "Ingrese los datos se inicio de sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ValidaUsuarioMySql extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            pbCargar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Databaseection success");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select usu.Id_Usuario, usu.Usuario, usu.Id_Rol, usu.Id_Ruta, rut.Ruta, " +
                        "uni.Unidad, uni.Placas From Apl_Usuarios usu INNER JOIN Cat_Rutas rut ON usu.Id_Ruta = rut.Id_Ruta INNER " +
                        "JOIN Cat_Unidades uni ON rut.Id_Unidad = uni.Id_Unidad Where usu.Usuario = '" +
                        txtUsuario.getText().toString() + "' and usu.Contraseña = '"+ txtContrasena.getText().toString()+"'");
                if(rs.next()) {
                    datosus = new clsUsuario();
                    datosus.Id_Usuario = rs.getInt(1);
                    datosus.Usuario = rs.getString(2);
                    datosus.Id_Rol = rs.getInt(3);
                    datosus.Ruta = rs.getString(5);
                    datosus.Unidad = rs.getString(6);
                    datosus.Placas = rs.getString(7);
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pbCargar.setVisibility(View.INVISIBLE);
            //Toast.makeText(getApplicationContext(), datosus.Usuario, Toast.LENGTH_SHORT).show();
            if (datosus != null) {
                EditarConf.putInt("Id_Usuario", datosus.getId_Usuario());
                EditarConf.putString("Usuario", datosus.getUsuario());
                EditarConf.putInt("Id_Rol", datosus.getId_Rol());
                EditarConf.putString("Encargado", datosus.getUsuario());
                EditarConf.putString("Ruta", datosus.getRuta());
                EditarConf.putString("Unidad", datosus.getUnidad());
                EditarConf.putString("Placas", datosus.getPlacas());
                EditarConf.commit();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
    }
}