#include <SPI.h>
//#include <String.h>
#include <Ethernet.h>
#include <Servo.h>;

#define pinLDR A1

//====== jardim =======
int pinLedJ1 = 22;
int pinLedJ2 = 24;
int pinLedJ3 = 26;
int pinLedJ4 = 28;
int pinLedJ5 = 30;
int pinLedJ6 = 32;

byte mac[] = { 0x90, 0xA2, 0xDA, 0x00, 0x9B, 0x36 };
byte ip[] = { 192, 168, 0, 110 };
EthernetServer server(80);

const int pres1 = 31; //PINO DIGITAL UTILIZADO PELO SENSOR DE PRESENÇA
const int pinChuva = 44; //PINO DIGITAL UTILIZADO PELO SENSOR DE chuva

const int valorAr = 815; // valor da humidade no ar
const int valorAgua = 490; //valor da humidade na água

const int pinSolo = A1;

int inter = (valorAr - valorAgua)/3;
int valorSolo = 0;

int led1 = 5;
int led2 = 6;
int led3 = 8;
// 34 36 38

int vent1 = 7;
int Fum1 = A0;

int servo1 = 3;
int controlS1 = 2;
boolean porta = false;

int nivelGas = 300; //VALOR LIMITE (NÍVEL DE GÁS NORMAL)

Servo s1;

// ======== alarme ==========
float seno;
int frequencia;
int pinAlarme = 9;
boolean ativaAlarme = false;
int controlAlarme = 33;


//float temp;
//const int lm35 = A0;

String readString = String(30);

String statusLed;

String statusFan;

String statusServo, statusPres, statusFum, statusChuva, statusSolo, statusAlarme;


void setup() {
  Ethernet.begin(mac, ip);
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(led3, OUTPUT);
  pinMode(Fum1, INPUT);
  pinMode(pinChuva, INPUT);
  pinMode(pinSolo, INPUT);
  pinMode(pinAlarme,OUTPUT);
  pinMode(controlAlarme, OUTPUT);

  pinMode(pinLedJ1, OUTPUT);
  pinMode(pinLedJ2, OUTPUT);
  pinMode(pinLedJ3, OUTPUT);
  pinMode(pinLedJ4, OUTPUT);
  pinMode(pinLedJ5, OUTPUT);
  pinMode(pinLedJ6, OUTPUT);
  
  pinMode(pres1, INPUT); 

  pinMode(vent1, OUTPUT);

  //servo motor
  pinMode(controlS1, OUTPUT);
  s1.attach(servo1);
  s1.write(0);

  //pinMode(lm35, INPUT);
}

void loop() {
  
  EthernetClient client = server.available();

  //temp = (float(analogRead(lm35))*5/(1023))/0.01;

  //float voltagem = ((analogRead(lm35))*5/(1023))/0.01;

  valorSolo = analogRead(pinSolo);

  int valorLDR = analogRead(pinLDR);

// =========================== Luz automatica jardim ====================================
  if(valorLDR > 500){
    digitalWrite(pinLedJ1, HIGH);
    digitalWrite(pinLedJ2, HIGH);
    digitalWrite(pinLedJ3, HIGH);
    digitalWrite(pinLedJ4, HIGH);
    digitalWrite(pinLedJ5, HIGH);
    digitalWrite(pinLedJ6, HIGH);
  }else{
    digitalWrite(pinLedJ1, LOW);
    digitalWrite(pinLedJ2, LOW);
    digitalWrite(pinLedJ3, LOW);
    digitalWrite(pinLedJ4, LOW);
    digitalWrite(pinLedJ5, LOW);
    digitalWrite(pinLedJ6, LOW);
  }
  
  if(client) 
  {
    while(client.connected())
    {
      if(client.available()) 
      {
        char c = client.read();
        
        if(readString.length() < 30) {
          readString += (c);
        }
        
        if(c == '\n')
        {
          
          if(readString.indexOf("led1") >= 0) {      // verifica se o botão foi pressionado e liga a respectiva porta no arduino 
            digitalWrite(led1, !digitalRead(led1));
          }
          
          if(readString.indexOf("led2") >= 0) {
            digitalWrite(led2, !digitalRead(led2));
          }
          
          if(readString.indexOf("led3") >= 0) {
            digitalWrite(led3, !digitalRead(led3));
          }

          if(readString.indexOf("vent1") >= 0) {
            digitalWrite(vent1, !digitalRead(vent1));
          }

          if(readString.indexOf("controlS1") >= 0) {
            digitalWrite(controlS1, !digitalRead(controlS1));
            //porta = true;
          }


          if(readString.indexOf("controlAlarme") >= 0) {
            digitalWrite(controlAlarme, !digitalRead(controlAlarme));         
          }

          if(readString.indexOf("pres1") >= 0) {
            digitalWrite(pres1, !digitalRead(pres1));
          }

          if(readString.indexOf("Fum1") >= 0) {
            analogWrite(Fum1, !analogRead(Fum1));
          }

          if(readString.indexOf("pinChuva") >= 0) {
            digitalWrite(pinChuva, !digitalRead(pinChuva));
          }

          if(readString.indexOf("pinSolo") >= 0) {
            analogWrite(pinSolo, !analogRead(pinSolo));
          }

    
            
          // http 
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/html");
          client.println();
         
          client.println("<!doctype html>");
          client.println("<html>");
          client.println("<head>");
          client.println("<title>Automação</title>");
          client.println("<meta name=\"viewport\" content=\"width=320\">");
          client.println("<meta name=\"viewport\" content=\"width=device-width\">");
          client.println("<meta charset=\"utf-8\">");
          client.println("<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">");
          client.println("<meta http-equiv=\"refresh\" content=\"2,URL=http://192.168.0.110\">");
          client.println("</head>");
          client.println("<body>");
          client.println("<center>");
          
          client.println("<font size=\"5\" face=\"verdana\" color=\"blue\">Automação residencial com</font>");
          client.println("<font size=\"5\" face=\"verdana\" color=\"blue\">Arduino e Android</font><br />");
          
          if(digitalRead(led1)) {
            statusLed = "Ligado";
          } else {
            statusLed = "Desligado";
          }
          client.println("<form action=\"led1\" method=\"get\">");
          client.println("<button type=submit style=\"width:200px;\">Led 1 - "+statusLed+"</button>");
          client.println("</form> <br />");
          
          if(digitalRead(led2)) {
            statusLed = "Ligado";
          } else {
            statusLed = "Desligado";
          }
          client.println("<form action=\"led2\" method=\"get\">");
          client.println("<button type=submit style=\"width:200px;\">Led 2 - "+statusLed+"</button>");
          client.println("</form> <br />");
          
          if(digitalRead(led3)) {
            statusLed = "Ligado";
          } else {
            statusLed = "Desligado";
          }
          client.println("<form action=\"led3\" method=\"get\">");
          client.println("<button type=submit style=\"width:200px;\">Led 3 - "+statusLed+"</button>");
          client.println("</form> <br />");

          //====================================== Temperatura =================================================
          client.println("<form>");
          //client.println("<b>Temperatura: </b>");
          //client.print(voltagem); 
          client.println("</form>");
          
          //====================================== ventilador =================================================
          if(digitalRead(vent1)) {
            statusFan = "Ligado";
          } else {
            statusFan = "Desligado";
          }
          client.println("<form action=\"vent1\" method=\"get\">");
          client.println("<button type=submit style=\"width:200px;\">Vent - "+statusFan+"</button>");
          client.println("</form> <br />");

          //====================================== Servo Motor =================================================
          //int p = s1.read();  
          //static boolean estado;
       
          if(digitalRead(controlS1)) {
            statusServo = "Ligado";
            s1.write(90);
          } else {
              statusServo = "Desligado";
              s1.write(0);        
          } 
          
          client.println("<form action=\"controlS1\" method=\"get\">");
          client.println("<button type=submit style=\"width:200px;\">Servo1 - "+statusServo+"</button>");
          client.println("</form> <br />");  

          //=================================== Alarme ==============================================


          if(digitalRead(controlAlarme)){
            //if(ativaAlarme == true) {
            statusAlarme = " ligado";
            ativaAlarme = true;
          }
          else {
            statusAlarme = " desligado";
            ativaAlarme = false;         
          }
          
          
          client.println("<form action=\"controlAlarme\" method=\"get\">");
          client.println("<button type=submit style=\"width:200px;\">Alarme - "+statusAlarme+"</button>");
          client.println("</form> <br />");


          //==================================== Presença ==============================================
          if(digitalRead(pres1)) {
            statusPres = "Presença detectada";
            alarme();
          } else {
            statusPres = "Nada detectado";
            desligaAlarme();
          }
          client.println("<form action=\"pres1\" method=\"get\">");
          client.println("<b>""Segurança - "+statusPres+"</b>");
          client.println("</form> <br />");

          //==================================== Fumaça ==============================================
          if(analogRead(Fum1) > nivelGas) {
            statusFum = "Fumaça detectada";
          } else {
            statusFum = "Nada detectado";
          }
          client.println("<form action=\"Fum1\" method=\"get\">");
          client.println("<b>""Incendio - "+statusFum+"</b>");
          client.println("</form> <br />");

          //==================================== Chuva ==============================================
          if(digitalRead(pinChuva) == 0){   
            statusChuva = "Chovendo";
          } else {
            statusChuva = "Sem chuva";
          }
          client.println("<form action=\"pinChuva\" method=\"get\">");
          client.println("<b>""Chuva - "+statusChuva+"</b>");
          client.println("</form> <br />");

          //==================================== Solo ==============================================
          if(valorSolo > valorAgua && valorSolo < (valorAgua + inter)){
            statusSolo = "Muito úmido";
          }
          else if(valorSolo > (valorAgua + inter) && valorSolo< (valorAr - inter)){
            statusSolo = "umidade adequada";
          }
          else if(valorSolo < valorAr && valorSolo > (valorAr - inter)){
            statusSolo = "Seco";
          }
          client.println("<form action=\"pinSolo\" method=\"get\">");
          client.println("<b>""Solo - "+statusSolo+"</b>");
          client.println("</form> <br />");

          
          
          client.println("</center>");
          client.println("</body>");
          client.println("</html>");
          
          readString = "";
          
          client.stop();
        }
      }
      
    }
  }
  
}

void alarme(){
  if(ativaAlarme == true && statusPres == "Presença detectada"){
    for(int x=0;x<180;x++){
      //converte graus para radiando e depois obtém o valor do seno
      seno=(sin(x*3.1416/180));
      //gera uma frequência a partir do valor do seno
      frequencia = 2000+(int(seno*1000));
      tone(pinAlarme,frequencia);
      delay(2);
    }
    noTone(pinAlarme);
  }
}

void desligaAlarme(){
  noTone(pinAlarme);
}
