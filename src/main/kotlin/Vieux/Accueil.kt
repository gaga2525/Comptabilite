package Vieux

import Model.Entity.DBController
import Model.Entity.MouvementModel
import Model.Entity.Mouvementbdd
import javafx.beans.property.SimpleDoubleProperty
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import tornadofx.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Accueil : View("Accueil") {
    var ttEntre = SimpleDoubleProperty(0.0)
    var ttSortie = SimpleDoubleProperty(0.0)
    var ttSolde = SimpleDoubleProperty(0.0)
    private var mois = 1

    val dbController: DBController by inject()
    private lateinit var checkMois: ComboBox<String>
    private lateinit var checkCategorie: ComboBox<String>
    private lateinit var checkMouvement: ComboBox<String>
    var MouvementTable: TableViewEditModel<MouvementModel> by singleAssign()
    var mouvements = dbController.mouvements

    override val root = vbox {
        menubar {
            menu("Fichier") {
                item("Nouveau"){
                    action {
                        find<NouvelEntrec>().openModal(stageStyle = StageStyle.UTILITY)}
                }
                item("Quitter"){
                    action {
                        replaceWith<Login>()
                    }
                }
            }
        }
        hbox {
            alignment = Pos.CENTER
            label ("Filtre par Mois   ")
            val mois = FXCollections.observableArrayList("Annee","Janvier",
                "Fevrier", "Mars","Avril","Mai", "Juin", "Juillet", "Aout",
                "Septembre","Octobre","Novembre","Decembre")
            checkMois = combobox<String> {
                items = mois
                valueProperty().addListener { _, _, newValue ->
                    println(newValue)
                }
            }

            label ("Filtre par Catagorie   ")
            val texasCities = FXCollections.observableArrayList("Tout","Salle",
                "Take a way", "Cyber","Poules","Oeuf","Anglais","Charcuterie","Divers")
            checkCategorie = combobox<String> {
                items = texasCities
                valueProperty().addListener { observable, oldValue, newValue ->
                    println(newValue)
                }
            }

        }
        vbox {
            alignment = Pos.CENTER
            style {
                backgroundColor += Color.AZURE
                backgroundInsets += box(4.px)
            }
            val vboxMouvement = tableview<MouvementModel>{
                MouvementTable = editModel
                items = mouvements
                column("Motif",MouvementModel::motif).contentWidth(padding = 10.0)
                column("Montant",MouvementModel::montant)
                column("Date du Mouvement",MouvementModel::datemvt)
                column("Categorie",MouvementModel::categorie)
                column("Type de Mouvement",MouvementModel::etatmvt)
                smartResize()
            }

            val data = SortedFilteredList(mouvements).bindTo(vboxMouvement)
            val donnneebb = transaction {
                Mouvementbdd.selectAll().toList()
            }
            checkMois.valueProperty().addListener { _, _, newValue ->
                when (newValue) {
                    "Aout" -> {checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 8
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==8)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==8) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==8)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==8)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==8)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==8)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==8)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==8)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==8 }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==8&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    }
                    }
                    "Janvier" -> {
                        checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                            mois = 1
                            when (newValue1) {
                                "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                        && (it.categorie.value.equals("Salle"))
                                }
                                    ttEntre.value=0.0
                                    ttSortie.value=0.0
                                    ttSolde.value =0.0
                                    donnneebb.forEach {
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Entree"
                                            &&it[Mouvementbdd.categorie]=="Salle"){
                                            ttEntre.value+=it[Mouvementbdd.montant]
                                        }
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Sortie"
                                            &&it[Mouvementbdd.categorie]=="Salle"){
                                            ttSortie.value+=it[Mouvementbdd.montant]
                                        }
                                        ttSolde.value = ttEntre.value - ttSortie.value
                                    }
                                }
                                "Take a way" -> {  data.predicate = { it ->
                                    (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                                }
                                    ttEntre.value=0.0
                                    ttSortie.value=0.0
                                    ttSolde.value =0.0
                                    donnneebb.forEach {
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Entree"
                                            &&it[Mouvementbdd.categorie]=="Take a way"){
                                            ttEntre.value+=it[Mouvementbdd.montant]
                                        }
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Sortie"
                                            &&it[Mouvementbdd.categorie]=="Take a way"){
                                            ttSortie.value+=it[Mouvementbdd.montant]
                                        }
                                        ttSolde.value = ttEntre.value - ttSortie.value
                                    }
                                }

                                "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                        && (it.categorie.value.equals("Cyber"))
                                }
                                    ttEntre.value=0.0
                                    ttSortie.value=0.0
                                    ttSolde.value =0.0
                                    donnneebb.forEach {
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Entree"
                                            &&it[Mouvementbdd.categorie]=="Cyber"){
                                            ttEntre.value+=it[Mouvementbdd.montant]
                                        }
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Sortie"
                                            &&it[Mouvementbdd.categorie]=="Cyber"){
                                            ttSortie.value+=it[Mouvementbdd.montant]
                                        }
                                        ttSolde.value = ttEntre.value - ttSortie.value
                                    }
                                }
                                "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                        && (it.categorie.value.equals("Poules"))
                                }
                                    ttEntre.value=0.0
                                    ttSortie.value=0.0
                                    ttSolde.value =0.0
                                    donnneebb.forEach {
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Entree"
                                            &&it[Mouvementbdd.categorie]=="Poules"){
                                            ttEntre.value+=it[Mouvementbdd.montant]
                                        }
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Sortie"
                                            &&it[Mouvementbdd.categorie]=="Poules"){
                                            ttSortie.value+=it[Mouvementbdd.montant]
                                        }
                                        ttSolde.value = ttEntre.value - ttSortie.value
                                    }
                                }
                                "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                        && (it.categorie.value.equals("Oeuf"))
                                }
                                    ttEntre.value=0.0
                                    ttSortie.value=0.0
                                    ttSolde.value =0.0
                                    donnneebb.forEach {
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Entree"
                                            &&it[Mouvementbdd.categorie]=="Oeuf"){
                                            ttEntre.value+=it[Mouvementbdd.montant]
                                        }
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Sortie"
                                            &&it[Mouvementbdd.categorie]=="Oeuf"){
                                            ttSortie.value+=it[Mouvementbdd.montant]
                                        }
                                        ttSolde.value = ttEntre.value - ttSortie.value
                                    }
                                }
                                "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                        && (it.categorie.value.equals("Anglais"))
                                }
                                    ttEntre.value=0.0
                                    ttSortie.value=0.0
                                    ttSolde.value =0.0
                                    donnneebb.forEach {
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Entree"
                                            &&it[Mouvementbdd.categorie]=="Anglais"){
                                            ttEntre.value+=it[Mouvementbdd.montant]
                                        }
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Sortie"
                                            &&it[Mouvementbdd.categorie]=="Anglais"){
                                            ttSortie.value+=it[Mouvementbdd.montant]
                                        }
                                        ttSolde.value = ttEntre.value - ttSortie.value
                                    }
                                }
                                "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                        && (it.categorie.value.equals("Charcuterie"))
                                }
                                    ttEntre.value=0.0
                                    ttSortie.value=0.0
                                    ttSolde.value =0.0
                                    donnneebb.forEach {
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Entree"
                                            &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                            ttEntre.value+=it[Mouvementbdd.montant]
                                        }
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Sortie"
                                            &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                            ttSortie.value+=it[Mouvementbdd.montant]
                                        }
                                        ttSolde.value = ttEntre.value - ttSortie.value
                                    }
                                }
                                "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                        && (it.categorie.value.equals("Divers"))
                                }
                                    ttEntre.value=0.0
                                    ttSortie.value=0.0
                                    ttSolde.value =0.0
                                    donnneebb.forEach {
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Entree"
                                            &&it[Mouvementbdd.categorie]=="Divers"){
                                            ttEntre.value+=it[Mouvementbdd.montant]
                                        }
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                            &&it[Mouvementbdd.etat]=="Sortie"
                                            &&it[Mouvementbdd.categorie]=="Divers"){
                                            ttSortie.value+=it[Mouvementbdd.montant]
                                        }
                                        ttSolde.value = ttEntre.value - ttSortie.value
                                    }
                                }
                                else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                    ttEntre.value=0.0
                                    ttSortie.value=0.0
                                    ttSolde.value =0.0
                                    donnneebb.forEach {
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                            ttEntre.value+=it[Mouvementbdd.montant]
                                        }
                                        if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                            ttSortie.value+=it[Mouvementbdd.montant]
                                        }
                                        ttSolde.value = ttEntre.value - ttSortie.value
                                    }
                                }
                            }
                        }
                    }
                    "Fevrier" -> { checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 2
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    } }
                    "Mars" -> { checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 3
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    } }
                    "Avril" -> { checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 4
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    } }
                    "Mai" -> { checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 5
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    } }
                    "Juin" -> {  checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 6
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    } }
                    "Juillet" -> {  checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 7
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    } }
                    "Decembre" -> { checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 12
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    } }
                    "Septembre" -> { checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 9
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    }  }
                    "Octobre" -> {  checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 10
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    }  }
                    "Novembre" -> {  checkCategorie.valueProperty().addListener { _, _, newValue1 ->
                        mois = 11
                        when (newValue1) {
                            "Salle" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Salle"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Salle"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Take a way" -> {  data.predicate = { it ->
                                (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois) && (it.categorie.value.equals("Take a way"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Take a way"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }

                            "Cyber" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Cyber"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Cyber"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Poules" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Poules"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Poules"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Oeuf" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Oeuf"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Oeuf"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Anglais" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Anglais"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Anglais"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Charcuterie" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Charcuterie"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Charcuterie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            "Divers" -> {  data.predicate = { it -> (LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois)
                                    && (it.categorie.value.equals("Divers"))
                            }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Entree"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois
                                        &&it[Mouvementbdd.etat]=="Sortie"
                                        &&it[Mouvementbdd.categorie]=="Divers"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                            else -> { data.predicate = { LocalDate.parse(it.datemvt.value, DateTimeFormatter.ISO_DATE).monthValue==mois }
                                ttEntre.value=0.0
                                ttSortie.value=0.0
                                ttSolde.value =0.0
                                donnneebb.forEach {
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Entree"){
                                        ttEntre.value+=it[Mouvementbdd.montant]
                                    }
                                    if(LocalDate.parse(it[Mouvementbdd.datemvt], DateTimeFormatter.ISO_DATE).monthValue==mois&&it[Mouvementbdd.etat]=="Sortie"){
                                        ttSortie.value+=it[Mouvementbdd.montant]
                                    }
                                    ttSolde.value = ttEntre.value - ttSortie.value
                                }
                            }
                        }
                    }}
                    else -> {
                        data.predicate = {
                            true
                        }
                        val entreList = transaction {
                            Mouvementbdd.select{Mouvementbdd.etat eq "Entree" }.toList()
                        }
                        entreList.forEach {
                            ttEntre.value+=it[Mouvementbdd.montant]
                        }
                        val sortieList = transaction {
                            Mouvementbdd.select{Mouvementbdd.etat eq "Sortie" }.toList()
                        }
                        sortieList.forEach {
                            ttSortie.value+=it[Mouvementbdd.montant]
                        }
                        ttSolde.value = ttEntre.value - ttSortie.value
                    }
                }
            }

        }
        hbox {
            vbox {
                label("Total entree : ")
                textfield().bind(ttEntre)
            }
            vbox {
                label("Total Sortie : ")
                textfield().bind(ttSortie)
            }
            vbox {
                label("solde : ")
                textfield().bind(ttSolde)
            }
        }
    }
}
 
