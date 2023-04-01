package com.example.sqmhiring

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder

import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.*
import com.example.sqmhiring.dao.EmployeeDao
import com.example.sqmhiring.database.DatabaseManager
import com.example.sqmhiring.ui.theme.SQMHiringTheme
import com.example.sqmhiring.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext

        val employeeDao = DatabaseManager.getDatabase(context).employeeDao()

        setContent {
            SQMHiringTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            Form(navController, employeeDao)
                        }
                        composable("success") {
                            Sucess(navController)
                        }
                        composable("all-employees") {

                            var employeesFemales: ArrayList<Employee> = ArrayList<Employee>()
                            var employeesMales: ArrayList<Employee> = ArrayList<Employee>()
                            GlobalScope.launch(Dispatchers.IO) {
                                employeesFemales = employeeDao.get3Females() as ArrayList<Employee>
                                employeesMales = employeeDao.get4Males() as ArrayList<Employee>
                            }

                            AllEmployees(navController = navController, employeesFemales = employeesFemales, employeesMales = employeesMales)

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AllEmployees(navController: NavController, employeesFemales: ArrayList<Employee>,  employeesMales: ArrayList<Employee>) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(text = "All Emplyes")
        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = "Females")
        Spacer(modifier = Modifier.padding(16.dp))
        if (employeesFemales.isEmpty()) {
        } else {


            employeesFemales.forEach { employee ->
                Row(modifier = Modifier.fillMaxWidth()) {

                    Text(text = "${employee.name}")
                    Text(text = "${employee.surname}")
                    Text(text = "${employee.gender}")

                    Column() {
                        employee.genres.forEach { genre ->
                            Text(text = genre)
                        }
                    }


                }
            }

        }
        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = "Males")
        Spacer(modifier = Modifier.padding(16.dp))
        if (employeesMales.isEmpty()) {
        } else {


            employeesMales.forEach { employee ->
                Row(modifier = Modifier.fillMaxWidth()) {

                    Text(text = "${employee.name}")
                    Text(text = "${employee.surname}")
                    Text(text = "${employee.gender}")

                    Column() {
                        employee.genres.forEach { genre ->
                            Text(text = genre)

                        }
                    }


                }
            }

        }
    }
}

@Composable
fun Sucess(navController: NavController){

    Column() {

        Row(modifier = Modifier.fillMaxWidth()) {

            Text(text = "Employee successfully register!")

        }
        
        Row(modifier = Modifier.fillMaxWidth()) {

            DoneButton(onSubmitClicked = {

                navController.navigate("all-employees")

            })

        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Add More")
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun SucessPreview() {

    val navController = rememberNavController()
    Sucess(navController)

}


@Composable
fun Form(navController: NavController, employeeDao: EmployeeDao) {

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var organization by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedOptions by remember { mutableStateOf(ArrayList<String>()) }

    val (selected, setSelected) = remember { mutableStateOf("") }

    var idcount = 0



    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

        Row(modifier = Modifier.fillMaxWidth()) {

            NameInputField(onTextChange = {name = it})

        }

        Row(modifier = Modifier.fillMaxWidth()) {


            SurnameInputField(onTextChange = {surname = it})
        }

        Row(modifier = Modifier.fillMaxWidth()) {


            OrganizationInputField(onTextChange = {organization = it})
        }

        Row(modifier = Modifier.fillMaxWidth()) {

            val genderList = listOf("Male", "Female", "Other")



            GenderDropDown(genderList = genderList, selectedGender = selectedGender, onGenderSelected = { gender ->
                selectedGender = gender
            })
        }

        Row(modifier = Modifier.fillMaxWidth()) {

            val options = listOf("Gospel", "Jazz", "RNB", "Amapiano")


            CheckboxList(
                options = options,
                selectedOptions = selectedOptions,
                onSelectionChanged = { selection ->
                    selectedOptions = selection as ArrayList<String>
                }
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {


            SubmitButton(onSubmitClicked = {
                // Code to handle submit button click


                var employee = Employee(

                    name = name,
                    surname = surname,
                    organization = organization,
                    gender = selectedGender,
                    genres = selectedOptions

                )

                name = ""
                surname = ""
                organization = ""
                selectedGender = ""
                selectedOptions.clear()


                GlobalScope.launch(Dispatchers.IO) {
                    employeeDao.insert(employee)
                    val employees = employeeDao.getAllEmployees()
                    employees.forEach { employee ->
                        Log.d("TAG", "Employee: $employee")
                    }
                }

                navController.navigate("success")


            })



            }

        Row(modifier = Modifier.fillMaxWidth()) {

            DoneButton(onSubmitClicked = {

                navController.navigate("all-employees")

            })

        }

    }


}



@Composable
fun NameInputField(onTextChange: (String) -> Unit) {
    var name by remember { mutableStateOf("") }

    OutlinedTextField(
        value = name,
        onValueChange = {
            name = it
            onTextChange(it)
        },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SurnameInputField(onTextChange: (String) -> Unit) {
    var surname by remember { mutableStateOf("") }

    OutlinedTextField(
        value = surname,
        onValueChange = {
            surname = it
            onTextChange(it)
        },
        label = { Text("Surname") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun OrganizationInputField(onTextChange: (String) -> Unit) {
    var surname by remember { mutableStateOf("") }

    OutlinedTextField(
        value = surname,
        onValueChange = {
            surname = it
            onTextChange(it)
        },
        label = { Text("Organization") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Gender(

    selected: String,
    setSelected: (selected: String) -> Unit,

    ){
    val radioOptions = listOf("Male","Female")
    var selectedItem by remember {
        mutableStateOf(radioOptions[0])
    }

    Column(modifier = Modifier.selectableGroup()){
        radioOptions.forEach{
                label ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (label == selected),
                        onClick = {
                            setSelected(label)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ){

                RadioButton(
                    modifier = Modifier.padding(end=16.dp),
                    selected = (selected==label),
                    onClick = {
                        setSelected(label)
                    },
                    enabled = true,
                )
                Text(text = label)
            }
        }
    }
}

@Composable
fun GenderDropDown(genderList: List<String>, selectedGender: String, onGenderSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Select Gender",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
        ) {
            Text(
                text = if (selectedGender.isEmpty()) "Select Gender" else selectedGender,
                color = if (selectedGender.isEmpty()) Color.Gray else Color.Black,
                modifier = Modifier.padding(16.dp)
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Arrow",
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genderList.forEach { gender ->
                DropdownMenuItem(
                    onClick = {
                        onGenderSelected(gender)
                        expanded = false
                    }
                ) {
                    Text(text = gender)
                }
            }
        }
    }
}

@Composable
fun CheckboxList(
    options: List<String>,
    selectedOptions: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    Column {

        Text(text = "Select Genres")

        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        val updatedSelection = if (selectedOptions.contains(option)) {
                            selectedOptions - option
                        } else {
                            selectedOptions + option
                        }
                        onSelectionChanged(updatedSelection)
                    }
            ) {
                Checkbox(
                    checked = selectedOptions.contains(option),
                    onCheckedChange = null,
                    modifier = Modifier.padding(8.dp)
                )

                Text(text = option)
            }
        }
    }
}

@Composable
fun SubmitButton(onSubmitClicked: () -> Unit) {
    Button(
        onClick = { onSubmitClicked() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Submit")
    }
}

@Composable
fun DoneButton(onSubmitClicked: () -> Unit) {
    Button(
        onClick = { onSubmitClicked() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Done")
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SQMHiringTheme {
        var textValue by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxWidth()) {

            Row(modifier = Modifier.fillMaxWidth()) {
                NameInputField(onTextChange = {textValue = it})

            }

            Row(modifier = Modifier.fillMaxWidth()) {
                SurnameInputField(onTextChange = {textValue = it})
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OrganizationInputField(onTextChange = {textValue = it})
            }



            Row(modifier = Modifier.fillMaxWidth()) {

                val genderList = listOf("Male", "Female", "Other")

                var selectedGender by remember { mutableStateOf("") }

                GenderDropDown(genderList = genderList, selectedGender = selectedGender, onGenderSelected = { gender ->
                    selectedGender = gender
                })
            }
            Row(modifier = Modifier.fillMaxWidth()) {

                val options = listOf("Gospel", "Jazz", "RNB", "Amapiano")
                var selectedOptions by remember { mutableStateOf(emptyList<String>()) }

                CheckboxList(
                    options = options,
                    selectedOptions = selectedOptions,
                    onSelectionChanged = { selection ->
                        selectedOptions = selection
                    }
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {



                SubmitButton(onSubmitClicked = {
                    // Code to handle submit button click
                })


            }


        }



    }
}

