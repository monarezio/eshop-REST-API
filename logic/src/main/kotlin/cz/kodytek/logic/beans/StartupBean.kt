package cz.kodytek.logic.beans

import cz.kodytek.logic.models.Category
import cz.kodytek.logic.models.Image
import cz.kodytek.logic.models.Product
import cz.kodytek.logic.models.ProductRating
import cz.kodytek.logic.models.invoice.DeliveryMethod
import cz.kodytek.logic.models.invoice.PaymentMethod
import cz.kodytek.logic.reader.CSVReader
import cz.kodytek.logic.services.interfaces.*
import java.io.File
import java.io.Serializable
import javax.annotation.PostConstruct
import javax.ejb.Singleton
import javax.ejb.Startup
import javax.ejb.TransactionManagement
import javax.ejb.TransactionManagementType
import javax.inject.Inject

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
open class StartupBean : Serializable {

    @Inject
    private lateinit var categoryService: ICategoryService

    @Inject
    private lateinit var productService: IProductService

    @Inject
    private lateinit var paymentMethodService: IPaymentMethodService

    @Inject
    private lateinit var deliveryMethodService: IDeliveryMethodService

    @Inject
    private lateinit var apiAccessKeyService: IApiAccessKeyService

    @Inject
    private lateinit var csvReader: CSVReader

    @Inject
    private lateinit var mailService: IMailService

    @PostConstruct
    open fun seed() {
        println("Creating api keys...")
        csvReader.read("/api-keys.csv") { line ->
            val accessKey = apiAccessKeyService.create(line.replace("\"", ""))
            mailService.sendPlainText("Samuel Kodytek", "samuel.kodytek@gmail.com", accessKey.email, "--- API KEY ---", """
                Hello, your access api key to the eshop web service is:
                
                ${accessKey.key}
                
                Please save it.
            """.trimIndent())
        }

        println("Seeding...")

        paymentMethodService.create(PaymentMethod(null, "Online kartou"))
        paymentMethodService.create(PaymentMethod(null, "Hotově/Kartou"))
        paymentMethodService.create(PaymentMethod(null, "Bankovní převod"))

        deliveryMethodService.create(DeliveryMethod(null, "Česká pošta", 2000))
        deliveryMethodService.create(DeliveryMethod(null, "DPL", 5000))
        deliveryMethodService.create(DeliveryMethod(null, "DPP", 9500))
        deliveryMethodService.create(DeliveryMethod(null, "Dobírka na prodejně", 0))

        val notebooks = categoryService.create(Category("Notebooky", """
            **Notebooky** umožňují práci i zábavu odkudkoli a to při zachování dostatečného výkonu. Jestli hledáte způsob, jak se podívat na internet, vyhledat si informace, hrát oblíbené hry, pracovat s různým software nebo prohlížet fotografie a videa, nesmí vám notebook chybět. Ať už přemýšlíte o notebooku do domácnosti, firemním notebooku, nebo si chcete pořídit herní notebook či zařízení 2v1, stále můžete vybírat z desítek a stovek produktů. V našem průvodci nastavení filtrů si notebooky můžete řadit dle výkonu, ceny, výrobce, ale třeba i barvy. Mezi nejznámější výrobce notebooků patří [HP](https://www.hpmarket.cz/), [Acer](https://www.acer.com/), [Asus](https://www.asus.com/), [Dell](http://www1.euro.dell.com/), [Lenovo](https://www.lenovo.com) a [Apple](https://www.apple.com/).

            Notebook pro domácnosti je ideální pro běžné požadavky - úpravu fotografií, sledování filmů, poslech hudby či prohlížení internetu.

            **Firemní notebook** je vhodný pro tvorbu dokumentů, tabulek a prezentací. Soustředíme se u něj zejména na výdrž baterie a svižnou práci v méně náročných programech, aby vás na cestách nic nebrzdilo. Neméně důležitým kritériem je možnost nadstandardního servisu či typ záruky. A pochopitelně volba operačního systému.

            **Herní notebook** je určen zejména pro hraní graficky náročných her. Bývá vybaven těmi nejvýkonnějšími komponenty v dané kategorii. U hráčských notebooků klademe kromě výkonu důraz i na chlazení a ergonomii přizpůsobenou pro hráče.

            **Zařízení 2v1** kladou důraz na kompaktní rozměry a nízkou hmotnost pro snadný přenos. Jedná se o zařízení podobné notebooku, které však navíc využívá výhod tabletu – má dotykový displej.

            Pro snadnou a bezpečnou mobilitu vašeho notebooku si u nás můžete vybrat z široké nabídky brašen a batohů. K notebookům také nabízíme náhradní napájecí zdroje a baterie. Zejména pro herní a výkonnější modely notebooků doporučujeme pro prodloužení životnosti používat chladící podložky.
        """.trimIndent()))

        val pcs = categoryService.create(Category("Stolní PC", """
            Ačkoliv dnešní trend směřuje k minimalizaci všech zařízení, tvoří **počítače** stále neoddělitelnou součást firem, ale i některých domácností. Jestli hledáte způsob, jak se podívat na internet, vyhledat si informace, hrát oblíbené hry, pracovat s různým software nebo prohlížet fotografie a videa, bude se vám rozhodně hodit počítač. Počítače využijete v domácnosti, firmě nebo ve veřejných institucích – školy, úřady, knihovny apod.
        """.trimIndent()))

        val externalDisks = categoryService.create(Category("Externí disky", """
            Kapacita **HDD** je výrobcem uváděna s předponou v desítkové soustavě, avšak některé PC systémy a operační systémy zobrazují kapacitu disku v rozšířených jednotkách v dvojkové soustavě. Skutečná kapacita disku se tak oproti uváděným údajům může lišit.
        """.trimIndent()))

        val consoles = categoryService.create(Category("Herní konzole", """
            **Herní konzole** se za několik posledních let staly nepostradatelnou součástí všech lidí, co mají rádi zábavu a hraní her v pohodlí domova. Hraní her si můžete užívat s přáteli a rodinou nebo v soukromí. V naší nabídce vybírejte stolní herní konzole PlayStation 4, PlayStation 3, XboX One, XboX 360 nebo Nintendo.
        """.trimIndent()))

        clearResourceFolder()
        addNotebooks(notebooks)
        addConsoles(consoles)
    }

    private fun clearResourceFolder() {
        val resources = File("resources")
        resources.deleteRecursively()
    }

    private fun addNotebooks(category: Category) {
        val mbpDescription = """
                    Pro ty, kdo překonávají hranice a mění svět, byl vyroben zdaleka nejvýkonnější notebook. Nový MacBook Pro se strhujícím 16palcovým Retina displejem, superrychlým procesorem, grafikou další generace, největší kapacitou akumulátoru ze všech MacBooků, novou Magic Keyboard a ohromným úložištěm. Je to vrcholný profesionální notebook pro nejnáročnější uživatele.
                    ###Displej
                    Nový MacBook Pro má fantastický 16palcový Retina displej – vůbec největší Retina displej ze všech notebooků Apple. Jas 500 nitů přispívá k vynikajícímu podání světlých tónů a zářivě jasné bílé. Přesné fotografické zarovnání molekul tekutých krystalů zase umožňuje zobrazení dokonale temné černé. Široký barevný gamut DCI-P3 zaručuje nádherně realistický obraz fotek a videí. Ať už jste kdekoli, svoji práci uvidíte v nejlepším možném světle.
                    ###Procesor
                    16palcový MacBook Pro přichází s úplně novou úrovní výkonu v notebooku. Díky vyspělejšímu termálnímu řešení může procesor Intel Core i9 až s osmi jádry a 16 vlákny udržet větší výkon delší dobu. Přitom dosahuje až 2,1krát vyššího výkonu než čtyřjádrový MacBook Pro. Ať vrstvíte desítky stop a efektů, renderujete 3D modely, nebo kompilujete a testujete kód, půjde vám to od ruky úplně samo.
                    ###Grafika
                    Grafika AMD Radeon Pro řady 5000M poskytuje vůbec nejvyšší grafický výkon v MacBooku Pro. Základní model 16palcového MacBooku Pro je proti základnímu modelu předchozí generace víc než dvakrát rychlejší. Video v ultravysokém rozlišení tak přehrává naprosto hladce a renderuje výrazně rychleji. S volitelnými 8 GB videopaměti GDDR6 získáte při zpracování úloh, jako je třeba barevný grading v DaVinci Resolve, až o 80 procent vyšší výkon proti Radeonu Pro Vega 20.
                """.trimIndent()

        val mbp13Description = """
            To nejlepší v jednom přístroji. Nádherně barevný Retina displej nabídne tak čisté barvy s úžasným kontrastem, že od něj nebudete chtít odtrhnout zrak. Rychlé nabíhání systému a okamžité reakce na každý váš povel vám pomohou dokončit práci ještě rychleji a vy si budete moci pustit film nebo zahrát náročnou 3D hru. Vše výborně uslyšíte díky špičkovým reproduktorům a stejně tak dobře budou všichni slyšet vás díky třem mikrofonům se směrovými paprsky. Abyste klávesnici využili na maximum, vyměnili vývojáři funkční tlačítka za přizpůsobivý Touch Bar. Nabíjet baterii a přesouvat soubory do jiných zařízení můžete přes všestranné porty Thunderbolt 3.
             ###Rychlý jako blesk
             Ať už jste počítačový expert nebo běžný uživatel, zcela jistě zaznamenáte absolutně bezchybný, rychlý a plynulý chod celého systému. I když svému MacBooku Pro dáte sebevíc zabrat, bez problému rozjede 3D grafické aplikace, náročné hry a snadno si poradí i s několika programy a aplikacemi najednou. Čtyřjádrový procesor Intel osmé generace se postará o to, aby práce byla rychle hotova a vy měli více času na zábavu.
             ###Lepší grafika
             Oproti předchozímu modelu si MacBook Pro polepšil i v grafice. Integrovaná grafika disponuje 128MB vestavěnou pamětí DRAM, což je dvakrát více, než kolik najdete u jeho staršího kolegy. Díky tomu jsou grafické úlohy značně urychleny a vy se tak můžete plně ponořit do práce bez jakéhokoli zdržování. 
             ###Touch Bar
             Místo funkčních kláves nyní najdete na horní straně klávesnice Touch Bar. Ten vám předloží přesně to, co právě potřebujete. Podle toho, co zrovna děláte, vám nabídne úpravu jasu a hlasitosti, predikci textu, výběr emotikonů nebo třeba interaktivní prvky na úpravu parametrů či prohlížení obsahu. Konečně můžete využívat celou klávesnici na sto procent. 
        """.trimIndent()

        val mbpParams = mapOf(
                Pair("Značka", "Apple"),
                Pair("Rozměry", "357,9 × 245,9 × 16,2"),
                Pair("Hmotnost", "2000 g"),
                Pair("Operační systém", "Apple macOS"),
                Pair("Uhlopříčka displeje", "16\""),
                Pair("Rozlišení displeje", "3072 × 1920 px"),
                Pair("Model procesor", "Intel Core i9-9880H"),
                Pair("Frekvence procesoru", "2300 MHz"),
                Pair("Model grafické karty", "AMD Radeon Pro 5500M")
        )

        val mbp13Params = mapOf(
                Pair("Značka", "Apple"),
                Pair("Rozměry", "357,9 × 245,9 × 16,2"),
                Pair("Hmotnost", "1370 g"),
                Pair("Operační systém", "Apple macOS"),
                Pair("Uhlopříčka displeje", "13.3\""),
                Pair("Rozlišení displeje", "2560 x 1600 px"),
                Pair("Model procesor", "Intel Core i5"),
                Pair("Frekvence procesoru", "1400 MHz"),
                Pair("Model grafické karty", "Intel Iris Plus Graphics 645")
        )

        val mbpImages = listOf(
                Image("/assets/macbookpro16/0.jpg"),
                Image("/assets/macbookpro16/1.jpg"),
                Image("/assets/macbookpro16/2.jpg")
        )

        val mbp13Images = listOf(
                Image("/assets/macbookpro13/0.jpg")
        )

        productService.create(
                Product(
                        null,
                        "Macbook pro 16\" 1000 GB",
                        mbpDescription,
                        196, 8299000,
                        mbpImages,
                        mbpParams + Pair("Kapaciat SSD", "1000 GB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Macbook pro 16\" 500 GB",
                        mbpDescription,
                        245, 8299000,
                        mbpImages,
                        mbpParams + Pair("Kapaciat SSD", "500 GB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Macbook pro 16\" 8 TB",
                        mbpDescription,
                        35, 8299000,
                        mbpImages,
                        mbpParams + Pair("Kapaciat SSD", "8 TB"),
                        listOf(
                                ProductRating(
                                        null,
                                        "Samuel",
                                        100,
                                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."),
                                ProductRating(
                                        null,
                                        "Tomáš",
                                        80,
                                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."),
                                ProductRating(
                                        null,
                                        "Karel",
                                        0,
                                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."),
                                ProductRating(null, null, 100, null),
                                ProductRating(null, null, 5, null)
                        )
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Macbook pro 16\" 4 TB",
                        mbpDescription,
                        24, 8299000,
                        mbpImages,
                        mbpParams + Pair("Kapaciat SSD", "8 TB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Macbook pro 13\" 124 GB",
                        mbp13Description,
                        43, 3499000,
                        mbp13Images,
                        mbp13Params + Pair("Kapaciat SSD", "124 GB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Macbook pro 13\" 258 GB",
                        mbp13Description,
                        12, 4499000,
                        mbp13Images,
                        mbp13Params + Pair("Kapaciat SSD", "258 GB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Macbook pro 13\" 500 GB",
                        mbp13Description,
                        3, 6499000,
                        mbp13Images,
                        mbp13Params + Pair("Kapaciat SSD", "500 GB"),
                        listOf()
                ), category
        )

        val dellInspironDescription = """
            Dell Inspiron 15 3000 (3593) je notebook s **pečlivě zvolenými komponenty pro perfektní výkon,** krásným elegantním černým designem a dlouhou výdrží baterie. To vše za dostupnou cenu. Že to není možné? Pojďte se přesvědčit.
            ###Vychutnejte si čistý obraz
            ledujte své oblíbené filmy a pořady na veliké 15,6" obrazovce v rozlišení Full HD. Ostré detaily, živé barvy a reálný kontrast.
            ###Navržen pro život
            Aby byly notebooky Dell Inspiron 15 (3000) skutečně spolehlivé, prochází důkladným testováním.
            * vystavení drsným podmínkám a teplotě až 65 °C
            * 20 000 otevření a zavření víka
            * 10 milionů úhozů kláves
            * 40 000 stisknutí multimediálních tlačítek
            * 25 000 otočení víka
            * 30 vyjmutí a vložení baterie
            ###Vysoká výdrž
            Díky dlouhé životnosti baterie můžete pracovat tak dlouho, jak jen budete potřebovat. Dokonce i bez přístupu k elektrické zásuvce.
        """.trimIndent()

        val dellInspironParams = mapOf(
                Pair("Značka", "Dell"),
                Pair("Velikost operační paměti RAM", "8 GB"),
                Pair("Hmotnost", "1370 g"),
                Pair("Operační systém", "Windows 10"),
                Pair("Uhlopříčka displeje", "15.6\""),
                Pair("Rozlišení displeje", "2560 x 1600 px"),
                Pair("Model procesor", "Intel Core i5 1035G1 Ice Lake"),
                Pair("Frekvence procesoru", "1000 MHz"),
                Pair("Model grafické karty", "Intel UHD Graphics")
        )

        val dellInspironImages = listOf(
                Image("/assets/dell-inspiron/0.jpg"),
                Image("/assets/dell-inspiron/1.jpg"),
                Image("/assets/dell-inspiron/2.jpg")
        )

        productService.create(
                Product(
                        null,
                        "Dell Inspiron 15 (3593) Silver",
                        dellInspironDescription,
                        3, 1599000,
                        dellInspironImages,
                        dellInspironParams,
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Dell Inspiron 15 (3593) Black",
                        dellInspironDescription,
                        3, 1599000,
                        dellInspironImages,
                        dellInspironParams,
                        listOf()
                ), category
        )

        val hpSpectreDescription = """
            ###Lákavý výkon
            **Tenký, výkonný a inteligentní**. HP Spectre x360 "13-aw" je **vybavený nejnovějším hardwarem** a baterií s neskutečnou výdrží, která zaručí možnost pracovat celý den. Podsvícená klávesnice zaručí ideální pracovní podmínky ve slabě osvětlených místnostech či během nočních letů.
            ###Otočný displej
            Díky inovativní konstrukci otočné o **360°** je možné používat zařízení ve čtyřech režimech. Věnujte se své práci v režimu notebooku, sledujte obrazovku v režimu podstavce, hrajte hry v režimu stojanu a na cestách používejte režim tabletu.
            ###HP Fast Charge
            Zapomeňte na omezování a mějte dostatek energie i v těch nejnáročnějších pracovních dnech!
            S technologií HP Fast Charge nabíjejte své zařízení s vybitou baterií z 0 na 50 % kapacity za cca 30 minut. Praktickou novinkou je **USB-C™ Thunderbolt™** port umístěný na pravém zadním zkoseném rohu, který umožní lepší manipulaci s napájecím kabelem.
            ###Další funkce
            | Revoluční, ultra tenký, prakticky bezrámečkový design s perfektním displejem a nádherným vzhledem. | O výjimečný audio projev se postará dvojice reproduktorů vyladěných profesionály z dílny Bang & Olufsen. | Přirozeně pracujte s obsahem, nápady a kresbami s využitím digitálního,pera a rozhraní Windows Ink. Pohotově zapisujte poznámky do aplikací,Office nebo kreslete do řady jiných aplikací. |
            |:--------------------------------------------------------------------------------------------------:|----------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
        """.trimIndent()

        val hpSpectreParams = mapOf(
                Pair("Značka", "HP"),
                Pair("Velikost operační paměti RAM", "16 GB"),
                Pair("Hmotnost", "1320 g"),
                Pair("Operační systém", "Windows 10"),
                Pair("Uhlopříčka displeje", "13.3\""),
                Pair("Rozlišení displeje", "3840 × 2160 px"),
                Pair("Model procesor", "Intel Core i7-1065G7"),
                Pair("Frekvence procesoru", "1300 MHz"),
                Pair("Model grafické karty", "Intel® Iris Plus (integrovaná bez vlastní paměti) ")
        )

        val hpSpectreImages = listOf(
                Image("/assets/hp-spectre/0.jpg"),
                Image("/assets/hp-spectre/1.jpg"),
                Image("/assets/hp-spectre/2.jpg"),
                Image("/assets/hp-spectre/3.jpg"),
                Image("/assets/hp-spectre/4.jpg")
        )

        productService.create(
                Product(
                        null,
                        "HP Spectre x360 13-aw0107nc",
                        hpSpectreDescription,
                        430,
                        4959900,
                        hpSpectreImages,
                        hpSpectreParams + Pair("Kapacita SSD", "1TB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "HP Spectre x360 15-df0009nc",
                        hpSpectreDescription,
                        430,
                        5199000,
                        hpSpectreImages,
                        hpSpectreParams + Pair("Kapacita SSD", "1TB") + Pair("Grafická karta", "NVIDIA GeForce GTX 1050 Ti/4GB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "HP Spectre x360 13-aw0108nc",
                        hpSpectreDescription,
                        430,
                        5299000,
                        hpSpectreImages,
                        hpSpectreParams + Pair("Kapacita SSD", "2TB"),
                        listOf()
                ), category
        )

        val lenovoThinkpasX1Description = """
            Zařízení ThinkPad bylo na vrcholu hory Everest, v hlubinách oceánu a deštných pralesích. NASA přijala ThinkPad na Mezinárodní vesmírnou stanici a vesmírnou stanici Mir. Je dokonce i ve stálé sbírce v Muzeu moderního umění v NY. Není tedy divu, že jsme prodali více než 100 milionů těchto spolehlivých zařízení – částečně i díky věrným zákazníkům řady X.
            
            Zařízení ThinkPad testujeme dle 12 armádních norem a provádíme více než 200 kontrol kvality, které zajišťují funkčnost těchto produktů i v extrémních podmínkách. Díky tomu se můžete spolehnout, že zařízení řady X zvládnou všechny překážky, které vám život postaví do cesty – od arktické divočiny přes pouštní písečné bouře, od stavu bez tíže až po potřísnění tekutinou a pády.

            > Řada ThinkPad se stále rozrůstá a vyvíjí s novými displeji a možnostmi připojení.
            > -TechAeris, 9. ledna 2018
            
            Ať už se jedná o zařízení 2v1, které se přeměňuje na tablet, tablet, který se stane notebookem, zařízení, které se přizpůsobí každému pracovnímu stylu díky pantu otočnému o 360°, je zde výkonný, lehký a snadno přenosný model X, který se hodí pro každého.
            
            Prvotřídní zpracování. Účelné provedení. Přeměnitelná zařízení 2v1. Oceněné klávesnice. Skvělé zabezpečení. Vysoký výkon. Vynikající výdrž baterie. Řada X vždy poskytuje prvotřídní zážitek.
        """.trimIndent()

        val lenovoX1Params = mapOf(
                Pair("Značka", "Lenovo"),
                Pair("Velikost operační paměti RAM", "16 GB"),
                Pair("Operační systém", "Windows 10"),
                Pair("Uhlopříčka displeje", "15.6\""),
                Pair("Rozlišení displeje", "3840 × 2160 px"),
                Pair("Model procesor", "Intel Core i9"),
                Pair("Model grafické karty", "Grafika NVIDIA® GeForce® GTX 1650 (MaxQ s 4GB GDDR5)")
        )

        val lenovoX1Images = listOf(
                Image("/assets/x1/0.jpg"),
                Image("/assets/x1/1.JPG"),
                Image("/assets/x1/2.JPG")
        )

        productService.create(
                Product(
                        null,
                        "ThinkPad X1 Extreme (2nd Gen) 500GB",
                        lenovoThinkpasX1Description,
                        43,
                        2799900,
                        lenovoX1Images,
                        lenovoX1Params + Pair("Velikost SSD", "500GB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "ThinkPad X1 Extreme (2nd Gen) 1TB",
                        lenovoThinkpasX1Description,
                        43,
                        2799900,
                        lenovoX1Images,
                        lenovoX1Params + Pair("Velikost SSD", "1000GB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "ThinkPad X1 Extreme (2nd Gen) 2TB",
                        lenovoThinkpasX1Description,
                        43,
                        2799900,
                        lenovoX1Images,
                        lenovoX1Params + Pair("Velikost SSD", "2000GB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "ThinkPad X1 Extreme (2nd Gen) 4TB",
                        lenovoThinkpasX1Description,
                        43,
                        2799900,
                        lenovoX1Images,
                        lenovoX1Params + Pair("Velikost SSD", "4000GB"),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "ThinkPad X1 Extreme (2nd Gen) 8TB",
                        lenovoThinkpasX1Description,
                        43,
                        2799900,
                        lenovoX1Images,
                        lenovoX1Params + Pair("Velikost SSD", "8000GB"),
                        listOf()
                ), category
        )


    }

    private fun addConsoles(category: Category) {
        productService.create(
                Product(
                        null,
                        "Nintendo Switch Black",
                        """
                            Mějte po ruce ten nejlepší prostředek herní zábavy, ať jste zrovna kdekoli, a užívejte si ji vždy v té nejlepší kvalitě. Herní konzole Nintendo Switch je navržena tak, abyste ji mohli používat kdekoli a kdykoli chcete. Snadno ji pomocí dokovací stanice připojíte ke své televizi a zahrajete si na velké obrazovce. Když se však chystáte na cesty, konzoli odpojíte a vezmete si ji s sebou. Její snadné ovládání vám zajistí multifukční ovladač, který funguje buď jako klasický joystick, případně se dá připojit k samotné konzoli. Hry si s radostí užijete i se svými přáteli, a to nejen díky chytrému joysticku, ale i zabudovanému Wi-Fi připojení. Vezměte si zábavu vždy tam, kam se zrovna chystáte!
                            ###Zabavte se doma i na cestách
                            S Nintendo Switch vás zábava jen tak nepřejde. Zařízení je designováno tak, aby vám nabídlo ty nejlepší multimediální možnosti, ať už jste kdekoli. Díky jeho unikátní konstrukci ho můžete buď připojit k televizi a používat jako domácí herní konzoli, nebo si ho vzít s sebou na cesty v podobě kompaktního přenosného přístroje. 
                            ###Multifunkční ovladač
                            Pokud si své hry zrovna užíváte prostřednictvím televize, je tu pro vás klasický dálkový joystick. Jste-li však na cestách, z Joy-Conu snadno odpojíte ovládací prvky a připojíte je ke konzoli – kompaktní, a přesto komfortně ovladatelný přístroj je na světě! 
                            ###Perfektní možnosti pro každého hráče
                            Nintendo Switch ve svém vybavení nabízí inovativní herní možnosti, díky kterým si užijete každý aspekt zábavy. Díky multifunkčnímu ovladači snadno proměníte jeden joystick ve dva, se svými přáteli si tak užijete tu nejlepší zábavu tváří v tvář. Kromě toho je součástí vybavení Wi-Fi připojení, díky kterému se spojíte až se sedmi dalšími Nintendo přístroji. 
                        """.trimIndent(),
                        94,
                        849000,
                        listOf(
                                Image("/assets/nintendo-switch/0.jpg"),
                                Image("/assets/nintendo-switch/1.jpg"),
                                Image("/assets/nintendo-switch/2.jpg"),
                                Image("/assets/nintendo-switch/3.jpg"),
                                Image("/assets/nintendo-switch/4.jpg"),
                                Image("/assets/nintendo-switch/5.jpg"),
                                Image("/assets/nintendo-switch/6.jpg")
                        ),
                        mapOf(
                                Pair("Značka", "Nintendo"),
                                Pair("Wifi", "Ano"),
                                Pair("GPU", " NVIDIA Tegra"),
                                Pair("CPU", " NVIDIA Tegra"),
                                Pair("Velkost displeje", "6.2 \""),
                                Pair("Rozslišení displeje", "1280 x 720")
                        ),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Nintendo Switch Red",
                        """
                            Mějte po ruce ten nejlepší prostředek herní zábavy, ať jste zrovna kdekoli, a užívejte si ji vždy v té nejlepší kvalitě. Herní konzole Nintendo Switch je navržena tak, abyste ji mohli používat kdekoli a kdykoli chcete. Snadno ji pomocí dokovací stanice připojíte ke své televizi a zahrajete si na velké obrazovce. Když se však chystáte na cesty, konzoli odpojíte a vezmete si ji s sebou. Její snadné ovládání vám zajistí multifukční ovladač, který funguje buď jako klasický joystick, případně se dá připojit k samotné konzoli. Hry si s radostí užijete i se svými přáteli, a to nejen díky chytrému joysticku, ale i zabudovanému Wi-Fi připojení. Vezměte si zábavu vždy tam, kam se zrovna chystáte!
                            ###Zabavte se doma i na cestách
                            S Nintendo Switch vás zábava jen tak nepřejde. Zařízení je designováno tak, aby vám nabídlo ty nejlepší multimediální možnosti, ať už jste kdekoli. Díky jeho unikátní konstrukci ho můžete buď připojit k televizi a používat jako domácí herní konzoli, nebo si ho vzít s sebou na cesty v podobě kompaktního přenosného přístroje. 
                            ###Multifunkční ovladač
                            Pokud si své hry zrovna užíváte prostřednictvím televize, je tu pro vás klasický dálkový joystick. Jste-li však na cestách, z Joy-Conu snadno odpojíte ovládací prvky a připojíte je ke konzoli – kompaktní, a přesto komfortně ovladatelný přístroj je na světě! 
                            ###Perfektní možnosti pro každého hráče
                            Nintendo Switch ve svém vybavení nabízí inovativní herní možnosti, díky kterým si užijete každý aspekt zábavy. Díky multifunkčnímu ovladači snadno proměníte jeden joystick ve dva, se svými přáteli si tak užijete tu nejlepší zábavu tváří v tvář. Kromě toho je součástí vybavení Wi-Fi připojení, díky kterému se spojíte až se sedmi dalšími Nintendo přístroji. 
                        """.trimIndent(),
                        94,
                        849000,
                        listOf(
                                Image("/assets/nintendo-switch/0.jpg"),
                                Image("/assets/nintendo-switch/1.jpg"),
                                Image("/assets/nintendo-switch/2.jpg"),
                                Image("/assets/nintendo-switch/3.jpg"),
                                Image("/assets/nintendo-switch/4.jpg"),
                                Image("/assets/nintendo-switch/5.jpg"),
                                Image("/assets/nintendo-switch/6.jpg")
                        ),
                        mapOf(
                                Pair("Značka", "Nintendo"),
                                Pair("Wifi", "Ano"),
                                Pair("GPU", " NVIDIA Tegra"),
                                Pair("CPU", " NVIDIA Tegra"),
                                Pair("Velkost displeje", "6.2 \""),
                                Pair("Rozslišení displeje", "1280 x 720")
                        ),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Nintendo Switch Lite Žlutá",
                        """
                            Odlehčená verze Nintenda Switch vychází rovnou ve třech barvách a jedné limitované edici. Kromě toho si k ní můžete pořídit i ochranné prvky, jako je ochranné sklo na displej nebo pouzdro. Můžete si tedy své oblíbené hry hrát i na cestách, aniž byste se obávali poškození konzole, nebo toho, že vám bude v batohu zabírat příliš mnoho místa. Těšit se můžete také na nové hry s oblíbenými Pokémony, novými postavami i spletitým záhadným příběhem. 
                            ###Lehčí verze oblíbené konzole
                             Konzole Nintendo Switch Lite je znatelně menší, lehčí a kompaktnější, než její starší bratr Nintendo Switch. Od hlavní verze se liší tím, že si na ní můžete zahrát pouze v Handheld módu, dále konzole nepodporuje výstup na TV, není kompatibilní s dokovací stanicí Nintendo Switch a nedisponuje ovladači Joy-Con. 
                            ###Tři barvy a limitovaná edice
                            Vybírat můžete ze tří barevných provedení. Vyrábí se ve žluté, šedé a tyrkysové variantě. Kromě toho je však dostupná i limitovaná edice Nintendo Switch Lite Zacian & Zamazenta, což je šedá konzole s azurovými a purpurovými tlačítky. Na zadní straně navíc najdete obrázky legendárních Pokémonů.  
                            ###Nové hry s Pokémony
                            Zároveň s konzolí se spouští hry Pokémon Sword a Pokémon Shield, ve kterých na vás čekají nová dobrodružství v regionu Galar. Budete moci chytat, bojovat i měnit Pokémony. Potkáte tu staré známé, ale i úplně nové postavy. A navíc musíte odhalit tajemství Zacian a Zamazenta, dvou pokémonů, které jistě velmi dobře znáte.  
                        """.trimIndent(),
                        94,
                        549000,
                        listOf(
                                Image("/assets/nintendo-switch-lite/0.jpg"),
                                Image("/assets/nintendo-switch-lite/1.jpg")
                        ),
                        mapOf(
                                Pair("Značka", "Nintendo"),
                                Pair("Wifi", "Ano"),
                                Pair("GPU", " NVIDIA Tegra"),
                                Pair("CPU", " NVIDIA Tegra"),
                                Pair("Velkost displeje", "5.5 \""),
                                Pair("Rozslišení displeje", "1280 x 720")
                        ),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Nintendo Switch Lite Limitovaná Edice",
                        """
                            Odlehčená verze Nintenda Switch vychází rovnou ve třech barvách a jedné limitované edici. Kromě toho si k ní můžete pořídit i ochranné prvky, jako je ochranné sklo na displej nebo pouzdro. Můžete si tedy své oblíbené hry hrát i na cestách, aniž byste se obávali poškození konzole, nebo toho, že vám bude v batohu zabírat příliš mnoho místa. Těšit se můžete také na nové hry s oblíbenými Pokémony, novými postavami i spletitým záhadným příběhem. 
                            ###Lehčí verze oblíbené konzole
                             Konzole Nintendo Switch Lite je znatelně menší, lehčí a kompaktnější, než její starší bratr Nintendo Switch. Od hlavní verze se liší tím, že si na ní můžete zahrát pouze v Handheld módu, dále konzole nepodporuje výstup na TV, není kompatibilní s dokovací stanicí Nintendo Switch a nedisponuje ovladači Joy-Con. 
                            ###Tři barvy a limitovaná edice
                            Vybírat můžete ze tří barevných provedení. Vyrábí se ve žluté, šedé a tyrkysové variantě. Kromě toho je však dostupná i limitovaná edice Nintendo Switch Lite Zacian & Zamazenta, což je šedá konzole s azurovými a purpurovými tlačítky. Na zadní straně navíc najdete obrázky legendárních Pokémonů.  
                            ###Nové hry s Pokémony
                            Zároveň s konzolí se spouští hry Pokémon Sword a Pokémon Shield, ve kterých na vás čekají nová dobrodružství v regionu Galar. Budete moci chytat, bojovat i měnit Pokémony. Potkáte tu staré známé, ale i úplně nové postavy. A navíc musíte odhalit tajemství Zacian a Zamazenta, dvou pokémonů, které jistě velmi dobře znáte.  
                        """.trimIndent(),
                        109,
                        549000,
                        listOf(
                                Image("/assets/nintendo-switch-lite/0.jpg"),
                                Image("/assets/nintendo-switch-lite/2.jpg")
                        ),
                        mapOf(
                                Pair("Značka", "Nintendo"),
                                Pair("Wifi", "Ano"),
                                Pair("GPU", " NVIDIA Tegra"),
                                Pair("CPU", " NVIDIA Tegra"),
                                Pair("Velkost displeje", "5.5 \""),
                                Pair("Rozslišení displeje", "1280 x 720")
                        ),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Nintendo Switch Lite Šedá",
                        """
                            Odlehčená verze Nintenda Switch vychází rovnou ve třech barvách a jedné limitované edici. Kromě toho si k ní můžete pořídit i ochranné prvky, jako je ochranné sklo na displej nebo pouzdro. Můžete si tedy své oblíbené hry hrát i na cestách, aniž byste se obávali poškození konzole, nebo toho, že vám bude v batohu zabírat příliš mnoho místa. Těšit se můžete také na nové hry s oblíbenými Pokémony, novými postavami i spletitým záhadným příběhem. 
                            ###Lehčí verze oblíbené konzole
                             Konzole Nintendo Switch Lite je znatelně menší, lehčí a kompaktnější, než její starší bratr Nintendo Switch. Od hlavní verze se liší tím, že si na ní můžete zahrát pouze v Handheld módu, dále konzole nepodporuje výstup na TV, není kompatibilní s dokovací stanicí Nintendo Switch a nedisponuje ovladači Joy-Con. 
                            ###Tři barvy a limitovaná edice
                            Vybírat můžete ze tří barevných provedení. Vyrábí se ve žluté, šedé a tyrkysové variantě. Kromě toho je však dostupná i limitovaná edice Nintendo Switch Lite Zacian & Zamazenta, což je šedá konzole s azurovými a purpurovými tlačítky. Na zadní straně navíc najdete obrázky legendárních Pokémonů.  
                            ###Nové hry s Pokémony
                            Zároveň s konzolí se spouští hry Pokémon Sword a Pokémon Shield, ve kterých na vás čekají nová dobrodružství v regionu Galar. Budete moci chytat, bojovat i měnit Pokémony. Potkáte tu staré známé, ale i úplně nové postavy. A navíc musíte odhalit tajemství Zacian a Zamazenta, dvou pokémonů, které jistě velmi dobře znáte.  
                        """.trimIndent(),
                        39,
                        549000,
                        listOf(
                                Image("/assets/nintendo-switch-lite/0.jpg"),
                                Image("/assets/nintendo-switch-lite/3.jpg")
                        ),
                        mapOf(
                                Pair("Značka", "Nintendo"),
                                Pair("Wifi", "Ano"),
                                Pair("GPU", " NVIDIA Tegra"),
                                Pair("CPU", " NVIDIA Tegra"),
                                Pair("Velkost displeje", "5.5 \""),
                                Pair("Rozslišení displeje", "1280 x 720")
                        ),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Nintendo Switch Lite Modrá",
                        """
                            Odlehčená verze Nintenda Switch vychází rovnou ve třech barvách a jedné limitované edici. Kromě toho si k ní můžete pořídit i ochranné prvky, jako je ochranné sklo na displej nebo pouzdro. Můžete si tedy své oblíbené hry hrát i na cestách, aniž byste se obávali poškození konzole, nebo toho, že vám bude v batohu zabírat příliš mnoho místa. Těšit se můžete také na nové hry s oblíbenými Pokémony, novými postavami i spletitým záhadným příběhem. 
                            ###Lehčí verze oblíbené konzole
                             Konzole Nintendo Switch Lite je znatelně menší, lehčí a kompaktnější, než její starší bratr Nintendo Switch. Od hlavní verze se liší tím, že si na ní můžete zahrát pouze v Handheld módu, dále konzole nepodporuje výstup na TV, není kompatibilní s dokovací stanicí Nintendo Switch a nedisponuje ovladači Joy-Con. 
                            ###Tři barvy a limitovaná edice
                            Vybírat můžete ze tří barevných provedení. Vyrábí se ve žluté, šedé a tyrkysové variantě. Kromě toho je však dostupná i limitovaná edice Nintendo Switch Lite Zacian & Zamazenta, což je šedá konzole s azurovými a purpurovými tlačítky. Na zadní straně navíc najdete obrázky legendárních Pokémonů.  
                            ###Nové hry s Pokémony
                            Zároveň s konzolí se spouští hry Pokémon Sword a Pokémon Shield, ve kterých na vás čekají nová dobrodružství v regionu Galar. Budete moci chytat, bojovat i měnit Pokémony. Potkáte tu staré známé, ale i úplně nové postavy. A navíc musíte odhalit tajemství Zacian a Zamazenta, dvou pokémonů, které jistě velmi dobře znáte.  
                        """.trimIndent(),
                        30,
                        549000,
                        listOf(
                                Image("/assets/nintendo-switch-lite/0.jpg"),
                                Image("/assets/nintendo-switch-lite/4.jpg")
                        ),
                        mapOf(
                                Pair("Značka", "Nintendo"),
                                Pair("Wifi", "Ano"),
                                Pair("GPU", " NVIDIA Tegra"),
                                Pair("CPU", " NVIDIA Tegra"),
                                Pair("Velkost displeje", "5.5 \""),
                                Pair("Rozslišení displeje", "1280 x 720")
                        ),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Sony Playstation 4 Pro 1TB",
                        """
                            Potykejte si s nejvýkonnější konzolí na světě a sdílejte své herní světy s ostatními. Model Pro z rodiny PlayStation 4 je k tomu jako stvořený! Zaujme vás nejen svým stylovým designem, ale především technologiemi, které skrývá uvnitř. Ne nadarmo je PS4 Pro snem každého hráče. Nabízí totiž bezkonkurenční výkon, podporu rozlišení 4K, špičkovou grafiku HDR s živými barvami a vylepšený ovladač DualShock 4, který si zamilujete pro jeho pohodlné a přesné ovládání. Vychutnejte si příliv energie, zábavy a relaxace v jednom. Vychutnejte si zážitek, který nemá obdoby.  
                            ###Stylový design
                            Futuristické tělo a výrazné křivky – to je provedení, které z této konzole udělá nejen skvělého společníka, ale i designový kousek, který perfektně zapadne do vaší domácnosti. Zastane i funkci Blu-ray přehrávače, který vám možná doteď chyběl, a navíc již v základu podporuje HDR u her i filmů.  
                            ###Pohlcující herní zážitek
                            Díky nejnovějším technologiím, které dřímají pod kapotou této konzole, bude hraní her na PS4 Pro neopakovatelným zážitkem. Dosyta si vychutnáte jak dokonale ostrý obraz, tak i perfektní detaily v každém okamžiku.  
                            ###Zábava, která nekončí
                            Nejlepší filmy, seriály a nejnovější videa budete moci streamovat ze služeb Netflix, YouTube i z řady dalších aplikací, a to až v dechberoucím rozlišení 4K! To vše díky automatickému převzorkování, které vám dopřeje ten nejrealističtější obraz.   
                        """.trimIndent(),
                        94,
                        896900,
                        listOf(
                                Image("/assets/playstation/0.jpg"),
                                Image("/assets/playstation/1.jpg")
                        ),
                        mapOf(
                                Pair("Značka", "Sony"),
                                Pair("Wifi", "Ano"),
                                Pair("GPU", "AMD Radeon"),
                                Pair("CPU", "AMD Jaguar"),
                                Pair("Velikost disku", "1TB")
                        ),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Sony Playstation 4 Pro 500GB",
                        """
                            Potykejte si s nejvýkonnější konzolí na světě a sdílejte své herní světy s ostatními. Model Pro z rodiny PlayStation 4 je k tomu jako stvořený! Zaujme vás nejen svým stylovým designem, ale především technologiemi, které skrývá uvnitř. Ne nadarmo je PS4 Pro snem každého hráče. Nabízí totiž bezkonkurenční výkon, podporu rozlišení 4K, špičkovou grafiku HDR s živými barvami a vylepšený ovladač DualShock 4, který si zamilujete pro jeho pohodlné a přesné ovládání. Vychutnejte si příliv energie, zábavy a relaxace v jednom. Vychutnejte si zážitek, který nemá obdoby.  
                            ###Stylový design
                            Futuristické tělo a výrazné křivky – to je provedení, které z této konzole udělá nejen skvělého společníka, ale i designový kousek, který perfektně zapadne do vaší domácnosti. Zastane i funkci Blu-ray přehrávače, který vám možná doteď chyběl, a navíc již v základu podporuje HDR u her i filmů.  
                            ###Pohlcující herní zážitek
                            Díky nejnovějším technologiím, které dřímají pod kapotou této konzole, bude hraní her na PS4 Pro neopakovatelným zážitkem. Dosyta si vychutnáte jak dokonale ostrý obraz, tak i perfektní detaily v každém okamžiku.  
                            ###Zábava, která nekončí
                            Nejlepší filmy, seriály a nejnovější videa budete moci streamovat ze služeb Netflix, YouTube i z řady dalších aplikací, a to až v dechberoucím rozlišení 4K! To vše díky automatickému převzorkování, které vám dopřeje ten nejrealističtější obraz.   
                        """.trimIndent(),
                        94,
                        846900,
                        listOf(
                                Image("/assets/playstation/0.jpg"),
                                Image("/assets/playstation/1.jpg")
                        ),
                        mapOf(
                                Pair("Značka", "Sony"),
                                Pair("Wifi", "Ano"),
                                Pair("GPU", "AMD Radeon"),
                                Pair("CPU", "AMD Jaguar"),
                                Pair("Velikost disku", "500GB")
                        ),
                        listOf()
                ), category
        )

        productService.create(
                Product(
                        null,
                        "Microsoft Xbox One 500GB",
                        """
                            S konzolí Xbox One okusíte zábavu, kterou nikde jinde nenajdete. Nabízí nejpokročilejší režim hry ve více hráčích a ty nejexkluzivnější hry vůbec. Díky funkci Smart Match najdete přesně takové vyzyvatele, kteří budou maximálně odpovídat vašemu hernímu stylu a dovednostem. Tento jedinečný kousek vám umožní také rychlé přepínání mezi zábavnými aplikacemi a hravě díky němu zvládnete i více činností najednou – umožní vám vedle sebe připnout film, hru i živé televizní vysílání.   
                            ###Jedinečná konzole
                            Rozbalte krabici a nechte se okouzlit konzolí Xbox One, která skvěle odpovídá nárokům dnešní hektické doby. Aktualizace i stahování probíhají na pozadí, takže vás nebude nic omezovat. Potřebujete stopnout rozehranou hru? Pro Xbox One to není problém. Pozastavte ji a potom pokračujte přesně tam, kde jste skončili.   
                            ###Bezdrátový ovladač
                            Xbox One se vám zavděčí také svým prvotřídním herním ovladačem, který prošel řadou inovací. Díky funkci Impulse Trigger, citlivým páčkám a směrovému ovladači okusíte ještě preciznější odezvu pro realističtější zážitek ze hry.   
                            ###Bezva poslech
                            Součástí balení jsou také pohodlná chatovací sluchátka, která se postarají o bezkonkurenčně čistý digitální zvuk. Přátele i protivníky tak budete moci poslouchat v té nejlepší kvalitě. Nastavení hlasitosti si navíc můžete snadno upravovat, aniž byste museli dát z rukou ovladač.    
                        """.trimIndent(),
                        52,
                        942000,
                        listOf(
                                Image("/assets/xbox/0.jpg"),
                                Image("/assets/xbox/1.jpg"),
                                Image("/assets/xbox/2.jpg"),
                                Image("/assets/xbox/3.jpg"),
                                Image("/assets/xbox/4.jpg"),
                                Image("/assets/xbox/5.jpg"),
                                Image("/assets/xbox/6.jpg")
                        ),
                        mapOf(
                                Pair("Značka", "Microsoft"),
                                Pair("Wifi", "Ano"),
                                Pair("GPU", "AMD Radeon GCN"),
                                Pair("CPU", "AMD Jaguar"),
                                Pair("Velikost disku", "500GB")
                        ),
                        listOf()
                ), category
        )
    }

}
