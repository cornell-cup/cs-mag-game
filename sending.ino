
/*int gLed = 10;
int yLed = 11;
int rLed = 12;*/
int led = 10;
int oled = 11;
char myCol[20];

void setup() {  
   Serial.begin (9600);  
   /*pinMode(gLed, OUTPUT);    
   pinMode(yLed, OUTPUT);   
   pinMode(rLed, OUTPUT);   
  
   digitalWrite(gLed, LOW);
   digitalWrite(yLed, LOW);
   digitalWrite(rLed, LOW); */
   pinMode(led,OUTPUT);
   digitalWrite(led,LOW);
   pinMode(oled,OUTPUT);
   digitalWrite(oled,LOW);
 
}


void loop() {
  int lf = 10;
  Serial.readBytesUntil(lf, myCol, 1);
    if(strcmp(myCol,"r")==0){
       digitalWrite(oled, HIGH);
       digitalWrite(led, LOW);
       delay(1000);
       digitalWrite(led, HIGH);
       digitalWrite(oled, LOW);
   }
  if(strcmp(myCol,"y")==0){
       digitalWrite(led, LOW);
       digitalWrite(oled, HIGH);
       digitalWrite(led, LOW); 
   }
  /*if(strcmp(myCol,"g")==0){
       digitalWrite(rLed, LOW);       
       digitalWrite(yLed, LOW);   
       digitalWrite(gLed, HIGH); 
   }*/
}

