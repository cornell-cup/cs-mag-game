
/*int gLed = 10;
int yLed = 11;
int rLed = 12;*/
int led = 13;
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
 
}


void loop() {
  int lf = 10;
  Serial.readBytesUntil(lf, myCol, 1);
    /*if(strcmp(myCol,"r")==0){
       digitalWrite(rLed, HIGH);       
       digitalWrite(yLed, LOW);   
       digitalWrite(gLed, LOW);*/
   }
  if(strcmp(myCol,"y")==0){
       digitalWrite(led, LOW);       
       digitalWrite(led, HIGH);   
       digitalWrite(led, LOW); 
   }
  /*if(strcmp(myCol,"g")==0){
       digitalWrite(rLed, LOW);       
       digitalWrite(yLed, LOW);   
       digitalWrite(gLed, HIGH); */
   }
}

