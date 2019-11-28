#include <SPI.h>
#include <MFRC522.h>
#include <Servo.h>;
#include <LiquidCrystal.h>
 
#define SS_PIN 10
#define RST_PIN 9
MFRC522 mfrc522(SS_PIN, RST_PIN);   

LiquidCrystal lcd(6, 7, 5, 4, 3, 2); 

char st[20];

boolean porta = false;

int servo1 = A0;

Servo s1;

int p = s1.read();
 
void setup() 
{
  Serial.begin(9600);   
  SPI.begin();      
  mfrc522.PCD_Init();   
  
  s1.attach(servo1);
  
  Serial.println("Aproxime o seu cartao do leitor...");
  Serial.println();

  s1.write(0);
  
  lcd.begin(16, 2);  
  mensageminicial();
}
void loop() 
{
  
  if ( ! mfrc522.PICC_IsNewCardPresent()) 
  {
    return;
  }
  
  if ( ! mfrc522.PICC_ReadCardSerial()) 
  {
    return;
  }

  Serial.print("UID tag :");
  String content= "";
  byte letter;
  for (byte i = 0; i < mfrc522.uid.size; i++) 
  {
     Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
     Serial.print(mfrc522.uid.uidByte[i], HEX);
     content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
     content.concat(String(mfrc522.uid.uidByte[i], HEX));
  }
  Serial.println();
  Serial.print("Message : ");
  content.toUpperCase();
  if (content.substring(1) == "86 5B 5D 1F" || content.substring(1) == "EB 6C 55 D3" || content.substring(1) == "84 EB 54 D3") //rfids aceitos
  {
    switch(porta){
          case false:
            s1.write(90);
            //statusServo == "Ligado";
            porta = true;
            Serial.println("Aberta");
            lcd.clear();
            lcd.setCursor(0,0);
            lcd.print("Bem vindo!");
            lcd.setCursor(0,1);
            lcd.print("Acesso liberado!");
            
          break;
          case true:
            s1.write(0);
            porta = false;
            Serial.println("fechada");
            lcd.clear();
            lcd.setCursor(0,0);
            lcd.print("Fechando a porta...");
            lcd.setCursor(0,1);
            lcd.print("Porta fechada!");
          break;
        }
    delay(3000);
  }
 
 else   {
    Serial.println(" Access denied");
    Serial.println();
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("Cartao invalido");
    lcd.setCursor(0,1);
    lcd.print("Acesso negado");
    delay(3000);
  }
} 

void mensageminicial()
{
  lcd.clear();
  lcd.print(" Aproxime o seu");  
  lcd.setCursor(0,1);
  lcd.print("cartao do leitor");  
}
