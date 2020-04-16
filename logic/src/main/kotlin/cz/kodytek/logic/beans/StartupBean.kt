package cz.kodytek.logic.beans

import cz.kodytek.logic.models.Category
import cz.kodytek.logic.services.interfaces.ICategoryService
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

    @PostConstruct
    open fun seed() {
        println("Seeding...")

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
    }

}
