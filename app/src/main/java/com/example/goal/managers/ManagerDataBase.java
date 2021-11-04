package com.example.goal.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe ManagerDataBase: Classe que controla o Banco de Dados Local. Ela possui uma Herança da
 * Classe {@link SQLiteOpenHelper SQLiteOpenHelper}, que permite ter uma banco de Dados Local no Mobile
 */
public class ManagerDataBase extends SQLiteOpenHelper {

    // Constantes Usadas no Banco de Dados
    public static final String DATABASE = "goalDB";
    public static final int VERSION_DATABASE = 1;
    public static final int TRUE = 1;
    public static final int FALSE = 0;

    // Constantes das Colunas da Tabela "User"
    public static final String TABLE_USER = "user";
    public static final String ID_USER = "id_user";
    public static final String NAME_USER = "name";
    public static final String NICKNAME_USER = "nickname";
    public static final String DOCUMENT_USER = "document";
    public static final String EMAIL_USER = "email";
    public static final String PASSWORD_USER = "password";
    public static final String PHONE_USER = "phone";
    public static final String DATE_BIRTH = "date_birth";
    public static final String IS_USER_SELLER = "is_seller";

    // Constantes das Colunas da Tabela "Wishes"
    public static final String TABLE_WISHES = "wishes";
    public static final String ID_PRODUCT = "id_product";

    // Constantes das Colunas da Tabela "Address"
    public static final String TABLE_ADDRESS = "address";
    public static final String ID_ADDRESS = "id_address";
    public static final String IS_BRASILIAN = "is_brasilian";
    public static final String ADDRESS = "address";
    public static final String DISTRICT = "district";
    public static final String STATE = "state";
    public static final String CITY = "city";
    public static final String NUMBER = "number_address";
    public static final String POSTAL_CODE = "postal_code";
    public static final String COUNTY = "country";

    // Constantes das Colunas da Tabela "Payment"
    public static final String TABLE_PAYMENT = "payment";
    public static final String TYPE_PAYMENT = "type_payment";
    public static final String VALUE_PAYMENT = "value_payment";

    /**
     * Instancia a Classe que controla o Banco de Dadso Local
     */
    public ManagerDataBase(Context context) {
        super(context, DATABASE, null, VERSION_DATABASE);
    }

    /**
     * Cria as Tabelas do Banco de Dados
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_USER + " (" +
                        ID_USER + " integer, " +
                        DOCUMENT_USER + " text, " +
                        NICKNAME_USER + " text, " +
                        EMAIL_USER + " text, " +
                        PASSWORD_USER + " text, " +
                        NAME_USER + " text, " +
                        PHONE_USER + " text, " +
                        DATE_BIRTH + " text, " +
                        IS_USER_SELLER + " integer)"
        );
        db.execSQL(
                "create table " + TABLE_WISHES + " (" +
                        VALUE_PAYMENT + " text, " +
                        ID_PRODUCT + " integer)"
        );
        db.execSQL(
                "create table " + TABLE_ADDRESS + " (" +
                        ID_ADDRESS + " integer, " +
                        ADDRESS + " text, " +
                        DISTRICT + " text, " +
                        STATE + " text, " +
                        CITY + " text, " +
                        NUMBER + " text, " +
                        POSTAL_CODE + " text, " +
                        COUNTY + " text, " +
                        IS_BRASILIAN + " integer)"
        );
        db.execSQL(
                "create table " + TABLE_PAYMENT + " (" +
                        TYPE_PAYMENT + " text)"
        );
    }

    /**
     * Atualiza a Versão do Banco de Dados. Limpa as Tabelas e Cria novamente o Banco de Dados
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        clearTables(db);
        this.onCreate(db);
    }

    /**
     * Remove todos os dados das Tabelas do Banco de Dados
     */
    private void clearTables(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHES);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);
    }
}
