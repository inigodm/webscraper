package inigo

import org.jsoup.Jsoup

class mainpage {

 val mainpageDoc = Jsoup.parse("""
    <!DOCTYPE html>
<html class="no-js" lang="es-ES">

<head>
            
        <title>Oportunidades LDLC</title>
        <meta charset="utf-8" />
    <script type="text/javascript" src="/v4/js/libs/jquery-3.2.1.min.js?5.0.0"></script>
</head>
    <body  data-language="es" data-country="es"
         data-country-label="España"
         data-error="Ha habido un error">

<div class="categories">
<div class="pic-bloc">
<a href="https://www.ldlc.com/es-es/oportunidades/c4684/" title="">
<img alt="Tarjeta gráfica" src="https://media.ldlc.com/r250/ld/categories/thumbnails/4684.jpg" height="231">
</a>
</div>
<div class="pic dsp-cell">
<p class="cat-bloc">
<a href="https://www.ldlc.com/es-es/oportunidades/c4265/" title="">Pantalla PC</a>
</p>
<div class="pic-bloc">
<a href="https://www.ldlc.com/es-es/oportunidades/c4265/" title="">
<img alt="Pantalla PC" height="231" src="https://media.ldlc.com/r250/ld/categories/thumbnails/4265.jpg">
</a>
</div>
<div class="bloc-jaune">
<a href="https://www.ldlc.com/es-es/oportunidades/c4265/" title=""> </a>

<p class="number-paragraph"><a href="https://www.ldlc.com/es-es/oportunidades/c4265/" title="">Ver</a></p>
<a href="https://www.ldlc.com/es-es/oportunidades/c4265/" title=""> </a>

<p class="item-paragraph"><a href="https://www.ldlc.com/es-es/oportunidades/c4265/" title="">más</a></p>
<a href="https://www.ldlc.com/es-es/oportunidades/c4265/" title=""> </a>
</div>
</div>
<div class="pic dsp-cell">
<p class="cat-bloc">
<a href="https://www.ldlc.com/es-es/oportunidades/c4416/" title="">Smartphones</a>
</p>
<div class="pic-bloc">
<a href="https://www.ldlc.com/es-es/oportunidades/c4416/" title="">
<img alt="Smartphones" height="231" src="https://media.ldlc.com/r250/ld/categories/thumbnails/4416.jpg">
</a>
</div>
<div class="bloc-jaune">
<a href="https://www.ldlc.com/es-es/oportunidades/c4416/" title=""> </a>

<p class="number-paragraph"><a href="https://www.ldlc.com/es-es/oportunidades/c4416/" title="">Ver</a></p>
<a href="https://www.ldlc.com/es-es/oportunidades/c4416/" title=""> </a>

<p class="item-paragraph"><a href="https://www.ldlc.com/es-es/oportunidades/c4416/" title="">más</a></p>
<a href="https://www.ldlc.com/es-es/oportunidades/c4416/" title=""> </a>
</div>
</div>
    </body>
</html>
""")
}
