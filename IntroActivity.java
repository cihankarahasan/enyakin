public class IntroActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    Animation bottomAnim;
    TextView appTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        appTxt = findViewById(R.id.appNameTxt);

        appTxt.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
