#include <IRremote.h>      

#define frente 0xFFA25D  // codigo recebido do controle ir
#define volta 0xFF629D   
#define para 0xFF22DD   

int receiver_pin = 2;      // pino receptor ir

int e_motor1 = 11;      //pino 6 do arduino paara pino 7 of l293d
int e_motor2 = 12;      //pino 7 do arduino para pino 2 of l293d

IRrecv receiver(receiver_pin); 
decode_results output;
unsigned long time;

void setup() {
  Serial.begin(9600);
  receiver.enableIRIn(); 
  pinMode(e_motor1, OUTPUT);
  pinMode(e_motor2, OUTPUT);
}

void loop() {
  if (receiver.decode(&output)) {
    unsigned int value = output.value;
    switch(value) {
      case frente:
           //digitalWrite(e_motor1,LOW);      
            digitalWrite(e_motor2,HIGH);
            delay(2000);  // depois de x tempo o motor para
            digitalWrite(e_motor2,LOW);                 
           break;
      case volta:
           digitalWrite(e_motor1,HIGH);
           delay(2000);  // depois de x tempo o motor para
           digitalWrite(e_motor1,LOW);
           //digitalWrite(e_motor2,LOW);          
           break;
      case para:
           digitalWrite(e_motor1,LOW);
           digitalWrite(e_motor2,LOW);     
           break;
    }
    receiver.resume();
  }
}
