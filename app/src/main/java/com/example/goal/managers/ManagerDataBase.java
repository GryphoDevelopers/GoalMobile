package com.example.goal.managers;

import static com.example.goal.managers.ManagerResources.isNullOrEmpty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.goal.R;
import com.example.goal.models.Address;
import com.example.goal.models.Product;
import com.example.goal.models.SerializationInfo;
import com.example.goal.models.User;

import java.util.ArrayList;
import java.util.List;

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
    public static final String IS_BRAZILIAN = "is_brazilian";
    public static final String ADDRESS = "address";
    public static final String DISTRICT = "district";
    public static final String STATE = "state";
    public static final String CITY = "city";
    public static final String NUMBER_ADDRESS = "number_address";
    public static final String POSTAL_CODE = "postal_code";
    public static final String COUNTY = "country";
    public static final String COMPLEMENT_ADDRESS = "complement";

    // Constantes das Colunas da Tabela "Payment"
    public static final String TABLE_PAYMENT = "payment";
    public static final String ID_PAYMENT = "id_payment";
    public static final String METHOD_PAYMENT = "method_payment";
    public static final String VALUE_PAYMENT = "value_payment";
    private static final int NOT_INSERT = -1;
    private static final int NOT_CHANGED = 0;
    // Variaveis usadas na Classe
    private final String MESSAGE_EXCEPTION;
    private final Context context;
    private String error_operation;

    /**
     * Construtor da classe {@link ManagerDataBase} herdado da classe {@link SQLiteOpenHelper}.
     * <p>
     * Obtem um {@link Context}, define o Nome do Banco de Dados e Sua Versão
     */
    public ManagerDataBase(Context context) {
        super(context, DATABASE, null, VERSION_DATABASE);
        this.context = context;
        // Define uma Mensagem Padrão de Erro
        error_operation = context.getString(R.string.error_generic);
        MESSAGE_EXCEPTION = context.getString(R.string.error_exception);
    }

    /**
     * Cria as Tabelas do Banco de Dados
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_USER + " (" +
                        ID_USER + " TEXT PRIMARY KEY, " +
                        DOCUMENT_USER + " TEXT, " +
                        NICKNAME_USER + " TEXT, " +
                        EMAIL_USER + " TEXT, " +
                        PASSWORD_USER + " TEXT, " +
                        NAME_USER + " TEXT, " +
                        PHONE_USER + " TEXT, " +
                        DATE_BIRTH + " TEXT, " +
                        IS_USER_SELLER + " INTEGER)"
        );
        db.execSQL(
                "create table " + TABLE_WISHES + " (" +
                        ID_PRODUCT + " TEXT)"
        );
        db.execSQL(
                "create table " + TABLE_ADDRESS + " (" +
                        ID_ADDRESS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ADDRESS + " TEXT, " +
                        DISTRICT + " TEXT, " +
                        STATE + " TEXT, " +
                        CITY + " TEXT, " +
                        NUMBER_ADDRESS + " TEXT, " +
                        POSTAL_CODE + " TEXT, " +
                        COUNTY + " TEXT, " +
                        COMPLEMENT_ADDRESS + " TEXT, " +
                        IS_BRAZILIAN + " INTEGER)"
        );
        db.execSQL(
                "create table " + TABLE_PAYMENT + " (" +
                        ID_PAYMENT + " integer PRIMARY KEY AUTOINCREMENT, " +
                        VALUE_PAYMENT + " text, " +
                        METHOD_PAYMENT + " text)"
        );
    }

    /**
     * Atualiza a Versão do Banco de Dados. Limpa as Tabelas e Cria novamente o Banco de Dados
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        clearTables();
        this.onCreate(db);
    }

    /**
     * Insere um {@link User} no Banco de Dados Local.
     * <p>
     * *Somente é possivel armazenar um unico Usuario no Banco de Dados. Se existir um usuario
     * ao executar esse comando, ele e seus dados serão apagados
     *
     * @param user Instancia da Classe Usuario que será inserida no Banco Local Mobile
     * @return true/false
     */
    public boolean insertUser(User user) {
        // Somente é possivel ter 1 Usuario Registrado no Banco de Dados
        if (amountInDatabase(TABLE_USER) > 0) clearTables();

        ContentValues values = setUpValuesUser(user);
        if (values == null) return false;

        return this.getWritableDatabase().insert(
                TABLE_USER, null, values) > NOT_INSERT;
    }

    /**
     * Insere um {@link Address} no Banco de Dados Local
     *
     * @param address Instancia da Classe de Endereço que será inserida no Banco Local Mobile
     * @return true/false
     */
    public boolean insertAddress(Address address) {
        return this.getWritableDatabase().insert(
                TABLE_ADDRESS, null, setUpValuesAddress(address)) > NOT_INSERT;
    }

    /**
     * Insere um {@link Product} no Banco de Dados Local
     *
     * @param id_product ID do Produto que será salva Localmente
     * @return true/false
     */
    public boolean insertWishes(String id_product) {
        ContentValues values = new ContentValues();
        values.put(ID_PRODUCT, id_product);

        return this.getWritableDatabase().insert(TABLE_WISHES, null, values) > NOT_INSERT;
    }

    /**
     * Atualiza o Unico Usuario inserido no Banco de Dados Local
     *
     * @param newUser Dados do Usuario que será atualizado
     * @return true/false
     */
    public boolean updateUser(User newUser) {
        // Compara as Informações Antigos com as Novas para ver quais serão atualizadas
        User userDatabase = this.getUserDatabase();

        if (userDatabase == null) return false;
        else if (!userDatabase.getId_user().equals(newUser.getId_user()))
            return insertUser(newUser);
        User finalUser = User.compareUser(userDatabase, newUser);

        ContentValues values = setUpValuesUser(finalUser);
        if (values == null) return false;

        return this.getWritableDatabase().update(TABLE_USER, values, ID_USER + "=?",
                new String[]{String.valueOf(finalUser.getId_user())}) > NOT_CHANGED;
    }

    /**
     * Obtem o ID de um {@link Address}, tanto para Enderços Brasileiros como Estrangeiros
     *
     * @param address Endereço que será buscado no banco de Dados
     * @return int
     */
    private int getIdAddress(Address address) {
        SQLiteDatabase database = this.getReadableDatabase();
        int id_recovery = 0;
        Cursor cursor;
        String whereClauses;
        String[] whereArgs;

        if (!address.getCountry().equals("Estrangeiro")) {
            whereClauses = String.format("$1%s=? AND $1%s=? $3%s=?", ADDRESS, DISTRICT, NUMBER_ADDRESS);
            whereArgs = new String[]{address.getAddress(), address.getDistrict(),
                    String.valueOf(address.getNumber())};
        } else {
            whereClauses = String.format("$1%s=? AND $1%s=? $3%s=?", POSTAL_CODE, ADDRESS, DISTRICT);
            whereArgs = new String[]{address.getUnmaskCep(), address.getAddress(),
                    address.getDistrict()};
        }

        cursor = database.query(TABLE_ADDRESS, new String[]{ID_ADDRESS}, whereClauses,
                whereArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            id_recovery = cursor.getInt(cursor.getColumnIndex(ID_ADDRESS));
            cursor.close();
        }
        return id_recovery;
    }

    /**
     * Obtem o Usuario do Banco de Dados
     *
     * @return User|null
     */
    public User getUserDatabase() {
        Cursor cursor = this.getReadableDatabase().query(TABLE_USER, null, null,
                null, null, null, null);

        SerializationInfo serializationInfo = new SerializationInfo(context);
        User userSerialized = serializationInfo.serializationUserDatabase(cursor);

        if (cursor != null && !cursor.isClosed()) cursor.close();
        if (userSerialized == null) error_operation = serializationInfo.getError_operation();

        return userSerialized;
    }

    /**
     * Verifica se um {@link Product} é um Produto da Lista de Desejos do Usuario
     *
     * @return true/false
     */
    public boolean isWishes(String id_product) {
        // Seleciona um Produto da Lista de Desejo no Banco de Dados
        Cursor cursor = this.getReadableDatabase().query(TABLE_WISHES, new String[]{ID_PRODUCT},
                ID_PRODUCT + "=?", new String[]{id_product}, null, null, null);

        // Serializa e Retorna se o Produto é ou não um Produto favoritado
        if (cursor != null && cursor.moveToFirst()) {
            String id_database = cursor.getString(cursor.getColumnIndex(ID_PRODUCT));
            cursor.close();
            return !isNullOrEmpty(id_database);
        } else return false;
    }

    /**
     * Obtem os IDs dos Produtos da Lista de Desejo
     *
     * @return {@link List}|null
     */
    public List<String> getIdWishes() {
        Cursor cursor = this.getReadableDatabase().query(TABLE_WISHES, new String[]{ID_PRODUCT},
                null, null, null, null, null);

        List<String> list_wishes = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list_wishes.add(cursor.getString(cursor.getColumnIndex(ID_PRODUCT)));
            }
            cursor.close();
        } else list_wishes = null;

        return list_wishes;
    }

    /**
     * Exclui um {@link Address} do Banco de Dados Local
     * <p>
     * * Retornará "false" caso não encontre o endereço
     *
     * @param address Endereço que será Obtido o ID e Excluido
     * @return true|false
     */
    public boolean deleteAddress(Address address) {
        int id_address = getIdAddress(address);
        if (id_address <= 0) return false;

        return this.getWritableDatabase().delete(TABLE_ADDRESS, ID_ADDRESS + "=?",
                new String[]{String.valueOf(id_address)}) > NOT_CHANGED;
    }

    /**
     * Exclui um Item da Lista de Desejos
     *
     * @param id_product ID do Produto que será removido
     * @return true|false
     */
    public boolean removeWishes(String id_product) {
        return this.getWritableDatabase().delete(
                TABLE_WISHES, ID_PRODUCT + "=?",
                new String[]{id_product}) > NOT_CHANGED;
    }

    /**
     * Cria um {@link ContentValues} de um {@link User} com os Valores Inseridos nas Keys
     *
     * @param user {@link User} que será utilizado os seus Valores
     * @return {@link ContentValues}|null
     */
    private ContentValues setUpValuesUser(User user) {
        try {
            ContentValues values = new ContentValues();
            values.put(ID_USER, user.getId_user());
            values.put(IS_USER_SELLER, user.isSeller());
            values.put(NAME_USER, user.getName());
            values.put(NICKNAME_USER, user.getNickname());
            values.put(EMAIL_USER, user.getEmail());
            values.put(PASSWORD_USER, user.getPassword());
            values.put(DATE_BIRTH, user.getString_dateBirth());

            if (isNullOrEmpty(user.getUnmaskCnpj()) && isNullOrEmpty(user.getUnmaskCpf())) {
                // Faz parte do Cadastro Completo (Opcional)
                values.put(DOCUMENT_USER, isNullOrEmpty(user.getUnmaskCnpj()) ? user.getUnmaskCpf() : user.getUnmaskCnpj());
                values.put(PHONE_USER, user.getUnmaskPhone());
            }
            return values;
        } catch (Exception ex) {
            error_operation = MESSAGE_EXCEPTION;
            Log.e("Exception", "ManagerDatabase - Inserção de Valores Interrompida");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Cria um {@link ContentValues} de um {@link Address} com os Valores Inseridos nas Keys
     *
     * @param address {@link Address} que será utilizado os seus Valores
     * @return {@link ContentValues}
     */
    private ContentValues setUpValuesAddress(Address address) {
        boolean is_foreign = address.getCountry().equals("Estrangeiro");

        ContentValues values = new ContentValues();
        values.put(IS_BRAZILIAN, is_foreign ? FALSE : TRUE);
        values.put(ADDRESS, address.getAddress());
        values.put(DISTRICT, address.getDistrict());
        values.put(STATE, address.getState());
        values.put(CITY, address.getCity());
        values.put(NUMBER_ADDRESS, address.getNumber());
        values.put(POSTAL_CODE, is_foreign ? "" : address.getUnmaskCep());
        values.put(COUNTY, address.getCountry());
        values.put(COMPLEMENT_ADDRESS, address.getComplement());

        return values;
    }

    /**
     * Obtem a Quantidade de Itens de uma Coluna em uma Tabela
     *
     * @param table Tabela que contem a Coluna (Column)
     * @return int
     */
    public int amountInDatabase(String table) {
        SQLiteDatabase database = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(database, table, null);
    }

    /**
     * Remove todos os dados das Tabelas do Banco de Dados
     */
    public void clearTables() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_USER, null, null);
        database.delete(TABLE_WISHES, null, null);
        database.delete(TABLE_ADDRESS, null, null);
        database.delete(TABLE_PAYMENT, null, null);
    }


    public String getError_operation() {
        return error_operation;
    }
}
