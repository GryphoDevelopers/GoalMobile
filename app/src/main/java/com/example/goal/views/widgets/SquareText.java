package com.example.goal.views.widgets;

import static com.example.goal.managers.ManagerResources.EXCEPTION;
import static com.example.goal.managers.ManagerResources.dpToPixel;
import static com.example.goal.managers.ManagerResources.isNullOrEmpty;
import static com.example.goal.managers.ManagerResources.spToPixel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.goal.R;

/**
 * View SquareText: View que exibirá dinamicamente multiplas opções de atributos de um Produto
 * (ex: Tamanho, Cor, etc)
 */
public class SquareText extends View {

    // Variaveis de Construções das Views
    private final TextPaint textPaint = new TextPaint();
    private final Paint paintStroke = new Paint();

    // Variaveis Usadas para Definir o Layout
    private String text_title = "";
    private float title_size;
    private int color_stroke;
    private int color_text;
    private float textWidth;
    private float textHeight;
    private boolean isSelected = false;

    /**
     * Construtor da Classe {@link SquareText} Utilizando apenas o Context como Parametro
     *
     * @param context {@link Context} utilizado na Construção da View
     */
    public SquareText(Context context) {
        super(context);
        init(null, 0);
    }

    /**
     * Construtor da Classe {@link SquareText} Utilizando o Context e Atributos como Parametros
     *
     * @param context {@link Context} utilizado na Construção da View
     * @param attrs   Atributos Utilizaddos na Configuração da View
     */
    public SquareText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    /**
     * Construtor da Classe {@link SquareText} Utilizando o Context, Atributos e Style como Parametros
     *
     * @param context  {@link Context} utilizado na Construção da View
     * @param attrs    Atributos Utilizaddos na Configuração da View
     * @param defStyle Estilo que será aplicado na View
     */
    public SquareText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * Construtor da Classe {@link SquareText} Utilizando o Context, Atributos e Style como Parametros
     * para construir a View de Forma Programatica (Via Codigo Java)
     *
     * @param context      {@link Context} utilizado na Construção da View
     * @param title        {@link #text_title Texto} que irá aparecer na View na medida "SP"
     * @param size_spTitle {@link #title_size Tamanho} que o Texto terá
     * @param color_text   {@link #color_text Cor do Texto} que será exibido
     * @param color_stroke {@link #color_stroke Cor da Borda} que será exibida
     */
    public SquareText(Context context, String title, int size_spTitle, int color_text,
                      int color_stroke) {
        super(context);
        init(null, 0);

        // Atualiza os Valores da View
        if (!isNullOrEmpty(title)) this.setTextTitle(title);
        if (size_spTitle > 0) this.setTitleSize(size_spTitle);
        if (color_text != 0) this.setTextColor(color_text);
        if (color_stroke != 0) this.setStrokeColor(color_stroke);
    }

    /**
     * Metodo Privado que inicia a Criação da View
     *
     * @param attrs    Atributos Utilizaddos na Configuração da View
     * @param defStyle Estilo que será aplicado na View
     */
    private void init(AttributeSet attrs, int defStyle) {
        try {
            // Obtem os Atributos/Estilo da View Inserida
            final TypedArray attr_array = getContext().obtainStyledAttributes(
                    attrs, R.styleable.SquareText, defStyle, 0);

            // Inserem os Valores colocados nos campos da View
            text_title = attr_array.getString(R.styleable.SquareText_textTitle);
            if (isNullOrEmpty(text_title)) text_title = "Example Text";

            title_size = attr_array.getDimension(R.styleable.SquareText_titleSize,
                    spToPixel(getContext(), 18));

            color_text = attr_array.getColor(R.styleable.SquareText_textColor,
                    getResources().getColor(R.color.black));
            color_stroke = attr_array.getColor(R.styleable.SquareText_strokeColor,
                    getResources().getColor(R.color.light_grey));

            // Permite que os Atributos/Estilos sejam Reutilizados
            attr_array.recycle();

            // Configura a criação de um Texto Customizado e da Borda da View
            textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            textPaint.setTextAlign(Paint.Align.LEFT);

            // Atualiza o Texto/Borda da View Confromme as Alerarações
            invalidate();

        } catch (Exception ex) {
            Log.e(EXCEPTION, "SquareText - Erro na Criação da View: " + ex.getClass().getName());
            ex.printStackTrace();
        }
    }

    /**
     * Metodo responsavel por atualizar os elementos da View conforme as Mudanças
     */
    @Override
    public void invalidate() {
        // Configura o Texto
        textPaint.setTextSize(title_size);
        textPaint.setColor(color_text);
        textWidth = textPaint.measureText(text_title);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textHeight = fontMetrics.bottom;

        paintStroke.setColor(color_stroke);
        paintStroke.setStrokeWidth(dpToPixel(getContext(), 2));
        paintStroke.setStyle(Paint.Style.STROKE);

        super.invalidate();
    }

    /**
     * Metodo responsavel por "Desenhar" a view na Tela com os Formatos e Estilo Definidos
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Obtem a Altura e Largura
        int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        // Insere o Texto centralizado, com os Atributos configurados
        canvas.drawText(text_title,
                getPaddingLeft() + (contentWidth - textWidth) / 2,
                getPaddingTop() + (contentHeight + textHeight) / 2,
                textPaint);

        // Insere a Borda
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintStroke);
    }


    // Getters e Setters da Classe e Atributos

    /**
     * Recupera o Texto exibido na {@link SquareText}
     *
     * @return {@link String}
     */
    public String getTextTitle() {
        return text_title;
    }

    /**
     * Define o Texto que será exibido na {@link SquareText}
     *
     * @param text_view Texto que será exibido
     */
    public void setTextTitle(String text_view) {
        this.text_title = text_view;
        invalidate();
    }

    /**
     * Retorna a cor utilizada na Borda da {@link SquareText}
     *
     * @return int
     */
    public int getStrokeColor() {
        return color_stroke;
    }

    /**
     * Define uma Cor para a Borda da {@link SquareText}
     *
     * @param stroke_color Cor utilziada na Borda
     */
    public void setStrokeColor(int stroke_color) {
        color_stroke = stroke_color;
        invalidate();
    }

    /**
     * Retorna a Cor utilziada no Texto Exibido
     *
     * @return int
     */
    public int getTextColor() {
        return color_text;
    }

    /**
     * Define uma Cor para o Texto Exibido
     *
     * @param text_color Valor da Cor utilzidada
     */
    public void setTextColor(int text_color) {
        color_text = text_color;
        invalidate();
    }

    /**
     * Retorna o Tamanho do Texto definido na View
     *
     * @return float
     */
    public float getTitleSize() {
        return title_size;
    }

    /**
     * Define o Tamanho do Titulo da View
     *
     * @param title_size Tamanho do Titulo da View em SP (Int)
     */
    public void setTitleSize(int title_size) {
        this.title_size = spToPixel(getContext(), title_size);
        invalidate();
    }


    /**
     * Manipula o Clique no {@link SquareText}, alterando a cor da borda e definindo a se foi
     * selecionado ou não
     */
    public void clickSquareText() {
        setStrokeColor(getResources().getColor(isSelected ? R.color.light_grey : R.color.lime_green));
        isSelected = !isSelected;
        invalidate();
    }

    /**
     * Retorna se a CustomView {@link SquareText} está selecionada ou não
     */
    public boolean isSelectedSquareText() {
        return isSelected;
    }

}