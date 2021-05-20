package com.example.myapplication.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.Question;
import com.example.myapplication.QuizContract;
import com.example.myapplication.bean.Reminder;
import com.example.myapplication.bean.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBSOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private final static int _DBVersion = 5; //<-- 版本
    private final static String _DBName = "fypdb";


    public DBSOpenHelper(Context context){
        super(context, _DBName,null,_DBVersion);
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS reminders(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "rseldate TEXT," +
                "rcontent TEXT," +
                "rcategory TEXT," +
                "status TEXT,"+
                "ralertime,TEXT"+
                "rimg TEXT,"+
                "addtime TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS tasks(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "mvname TEXT," +
                "mvimg TEXT,"+
                "mvcategory TEXT," +
                "mvpath TEXT," +
                "status TEXT," +
                "addtime TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                QuizContract.QuestionsTable.TABLE_NAME + " ( " +
                QuizContract.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.QuestionsTable.TASK_ID + " TEXT, "+
                QuizContract.QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ");");

        //初始化
        initDataBase(db);
    }

    private void initDataBase (SQLiteDatabase db) {
        ContentValues cv_mv1 = new ContentValues();
        cv_mv1.put("mvname","如何用臉書(Facebook)");
        cv_mv1.put("mvimg","https://p.ssl.qhimg.com/d/dy_bf020142f4570ed7a44b49698a52b122.jpg");
        cv_mv1.put("mvcategory","youtube");
        cv_mv1.put("mvpath","gpaDzhVBLvc");
        cv_mv1.put("status","0");
        cv_mv1.put("addtime","");
        db.insert("tasks", "_id", cv_mv1);

        ContentValues cv_mv2 = new ContentValues();
        cv_mv2.put("mvname","如何用Whatsappi之一");
        cv_mv2.put("mvimg","https://p.ssl.qhimg.com/d/dy_392bf49b5001e790fa26e8a5f14f2555.jpg");
        cv_mv1.put("mvcategory","youtube");
        cv_mv2.put("mvpath","9ajW0Kxel2M");
        cv_mv2.put("status","0");
        cv_mv2.put("addtime","");
        db.insert("tasks", "_id", cv_mv2);

        ContentValues cv_mv3 = new ContentValues();
        cv_mv2.put("mvname","如何用Whatsapp之二");
        cv_mv2.put("mvimg","");
        cv_mv1.put("mvcategory","youtube");
        cv_mv2.put("mvpath","rEcNo1e0oAI");
        cv_mv2.put("status","0");
        cv_mv2.put("addtime","");
        db.insert("tasks", "_id", cv_mv3);

        ContentValues cv_mv4 = new ContentValues();
        cv_mv2.put("mvname","如何用Whatsapp之三");
        cv_mv2.put("mvimg","");
        cv_mv1.put("mvcategory","youtube");
        cv_mv2.put("mvpath","pYzccMTkLW4");
        cv_mv2.put("status","0");
        cv_mv2.put("addtime","");
        db.insert("tasks", "_id", cv_mv4);

        ContentValues cv_mv5 = new ContentValues();
        cv_mv2.put("mvname","如何用ZOOM");
        cv_mv2.put("mvimg","");
        cv_mv1.put("mvcategory","youtube");
        cv_mv2.put("mvpath","SaqOWMczFFY");
        cv_mv2.put("status","0");
        cv_mv2.put("addtime","");
        db.insert("tasks", "_id", cv_mv5);

        ContentValues cv_mv6 = new ContentValues();
        cv_mv2.put("mvname","如何用轉數快");
        cv_mv2.put("mvimg","");
        cv_mv1.put("mvcategory","youtube");
        cv_mv2.put("mvpath","eicoNqqbHQ");
        cv_mv2.put("status","0");
        cv_mv2.put("addtime","");
        db.insert("tasks", "_id", cv_mv6);

        ContentValues cv_mv7 = new ContentValues();
        cv_mv2.put("mvname","如何用網上購物使用電子支付(PAYME)");
        cv_mv2.put("mvimg","");
        cv_mv1.put("mvcategory","youtube");
        cv_mv2.put("mvpath","wIiwiR4OCMU");
        cv_mv2.put("status","0");
        cv_mv2.put("addtime","");
        db.insert("tasks", "_id", cv_mv7);

        ContentValues cv_mv8 = new ContentValues();
        cv_mv2.put("mvname","如何用瀏覽器");
        cv_mv2.put("mvimg","");
        cv_mv1.put("mvcategory","youtube");
        cv_mv2.put("mvpath","Yfd_TP1auuo");
        cv_mv2.put("status","0");
        cv_mv2.put("addtime","");
        db.insert("tasks", "_id", cv_mv8);

        com.example.myapplication.Question q1 = new com.example.myapplication.Question("藍色咪代表甚麼呢", "4","A.錄音按鈕 ", "B.播放鍵", "C.對方已聽了您的錄音", 3);
        addQuestion(q1);
        com.example.myapplication.Question q2 = new com.example.myapplication.Question("兩個灰色剔代表甚麼呢","4", "A.對方已收到你的訊息", "B.對方讀了你的訊息", "C.對方已離線", 1);
        addQuestion(q2);
    }
    private void addQuestion(com.example.myapplication.Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuizContract.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizContract.QuestionsTable.TASK_ID, question.getTask_id());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuizContract.QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuizContract.QuestionsTable.TABLE_NAME, QuizContract.QuestionsTable._ID, cv);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (newVersion > oldVersion) {

                    // 2. 刪掉UserData資料表
                    db.execSQL("DROP TABLE IF EXISTS reminders");
                    db.execSQL("DROP TABLE IF EXISTS tasks");
                    db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionsTable.TABLE_NAME);
                    // 3. 建立新的UserData
                    onCreate(db);

        }
    }
    /**
     * 接下来写自定义的增删改查方法
     */
    public List<Question> getQuestions(int id) {
        List<com.example.myapplication.Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.query(QuizContract.QuestionsTable.TABLE_NAME,null,"TASK_ID="+""+id,null,null,null,"_id desc");

        if (c.moveToFirst()) {
            do {
                com.example.myapplication.Question question = new com.example.myapplication.Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_QUESTION)));
                question.setTask_id(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.TASK_ID)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public boolean addReminder(String seldate,String content,String category,String img,String alerTime){
        ContentValues values = new ContentValues();
        values.put("rseldate", seldate);
        values.put("rcontent", content);
        values.put("status",0);
        values.put("rcategory",category);
        values.put("ralertime",alerTime);
        values.put("rimg",img);
        return db.insert("reminders", null, values) >0 ;
    }

    public List<Reminder> getAvbReminder(){
        List<Reminder> list = new ArrayList<>();
        Cursor cursor = db.query("reminders",null,"status=0",null,null,null,"_id desc");
        while (cursor.moveToNext()){
            Reminder data = new Reminder();
            data.setrId(cursor.getInt(cursor.getColumnIndex("_id")));
            data.setrSeldate(cursor.getString(cursor.getColumnIndex("rseldate")));
            data.setrContent(cursor.getString(cursor.getColumnIndex("rcontent")));
            data.setrCategory(cursor.getString(cursor.getColumnIndex("rcategory")));
            data.setrStatus(cursor.getInt(cursor.getColumnIndex("status")));
            data.setrAddtime(cursor.getString(cursor.getColumnIndex("addtime")));
            data.setrImg(cursor.getString(cursor.getColumnIndex("rimg")));
            data.setrAlerTime(cursor.getString(cursor.getColumnIndex("ralertime")));
            list.add(data);
        }
        return list;
    }
    public int findId(String seldate,String alerTime){
        Cursor cursor = db.query("reminders",null,"rseldate = "+seldate+" and ralertime ="+alerTime,null,null,null,"_id desc");
        int findid=-1;
        while (cursor.moveToNext()){
            findid=(cursor.getInt(cursor.getColumnIndex("_id")));
        }
        return findid;
    }
    public Reminder getOneReminder(int id){
        Cursor cursor = db.query("reminders",null,"status=0 and _id = "+id,null,null,null,"_id desc");
        Reminder data = new Reminder();
        while (cursor.moveToNext()){
            data.setrId(cursor.getInt(cursor.getColumnIndex("_id")));
            data.setrSeldate(cursor.getString(cursor.getColumnIndex("rseldate")));
            data.setrContent(cursor.getString(cursor.getColumnIndex("rcontent")));
            data.setrCategory(cursor.getString(cursor.getColumnIndex("rcategory")));
            data.setrStatus(cursor.getInt(cursor.getColumnIndex("status")));
            data.setrAddtime(cursor.getString(cursor.getColumnIndex("addtime")));
            data.setrImg(cursor.getString(cursor.getColumnIndex("rimg")));
            data.setrAlerTime(cursor.getString(cursor.getColumnIndex("ralertime")));
        }
        return data;
    }

    public int delOneReminder(int id){
        return db.delete("reminders","id=? "+id,new String[]{""+id});
    }
    public boolean updateOneReminder(int id,String seldate,String content,String category,String img,String alerTime){
        ContentValues values = new ContentValues();
        values.put("rseldate", seldate);
        values.put("rcontent", content);
        values.put("status",0);
        values.put("rcategory",category);
        values.put("ralertime",alerTime);
        values.put("rimg",img);
        return db.update("reminders", values,"_id = ?", new String[]{""+id}) > 0;
    }


    public Task getOneTask(int id){
        Cursor cursor = db.query("tasks",null,"status=0 and _id = "+id,null,null,null,"_id desc");
        Task data = new Task();
        while (cursor.moveToNext()){
            data.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            data.setMvname(cursor.getString(cursor.getColumnIndex("mvname")));
            data.setMvimg(cursor.getString(cursor.getColumnIndex("mvimg")));
            data.setMvpath(cursor.getString(cursor.getColumnIndex("mvpath")));
            data.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            data.setAddtime(cursor.getString(cursor.getColumnIndex("addtime")));;
        }
        return data;
    }

    public List<Task> getTasks(){
        List<Task> list = new ArrayList<>();
        Cursor cursor = db.query("tasks",null,null,null,null,null,"_id desc");
        while (cursor.moveToNext()){
            Task data = new Task();
            data.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            data.setMvname(cursor.getString(cursor.getColumnIndex("mvname")));
            data.setMvimg(cursor.getString(cursor.getColumnIndex("mvimg")));
            data.setMvpath(cursor.getString(cursor.getColumnIndex("mvpath")));
            data.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            data.setAddtime(cursor.getString(cursor.getColumnIndex("addtime")));
            list.add(data);
        }
        return list;
    }

    public List<Task> getHistoryTasks(){
        List<Task> list = new ArrayList<>();
        Cursor cursor = db.query("tasks",null,"status=1",null,null,null,"_id desc");
        while (cursor.moveToNext()){
            Task data = new Task();
            data.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            data.setMvname(cursor.getString(cursor.getColumnIndex("mvname")));
            data.setMvimg(cursor.getString(cursor.getColumnIndex("mvimg")));
            data.setMvpath(cursor.getString(cursor.getColumnIndex("mvpath")));
            data.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            data.setAddtime(cursor.getString(cursor.getColumnIndex("addtime")));
            list.add(data);
        }
        return list;
    }

    public int getAllFinishNum(){
        Cursor cursor = db.query("tasks",null,"status=1",null,null,null,"_id desc");
        int count=0;
        while (cursor.moveToNext()){
            count=count+1;
        }
        return count;
    }

    public int getTodayFinishNum(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate =  new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Cursor cursor = db.query("tasks",null,"status=1 and addtime like '"+str+"%'",null,null,null,"_id desc");
        int count=0;
        while (cursor.moveToNext()){
            count=count+1;
        }
        return count;
    }

    public boolean updateTaskStatus(int taskid){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        ContentValues values = new ContentValues();
        values.put("status",1);
        values.put("addtime",str);
        return db.update("tasks",values,"_id=?",new String[]{taskid+""}) > 0;
    }

}

