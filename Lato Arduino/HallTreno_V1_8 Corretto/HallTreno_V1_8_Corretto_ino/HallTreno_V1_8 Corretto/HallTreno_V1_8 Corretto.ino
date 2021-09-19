#include <Servo.h> 

Servo myservo;
int pinServo=7;
int pinDirA=12; //A motore treno
int pinVelA=3;  // B motore scambio
int pinBrakeA=9;
//int pinDirB=13;
//int pinVelB=11;
//int pinBrakeB=8;
int vel=200;
int contaFermate=2;
int hall=0;
char comando;
boolean autoMode;
boolean verso=true;
boolean metroMovimento;
unsigned long tempo,tempoFermato;
int controlloFermate;
int gradiSu=100;
int gradiGiu=0;

void setup()
 {
 Serial.begin(9600);// con seriale
 pinMode(pinDirA,OUTPUT);
 pinMode(pinVelA,OUTPUT);
 pinMode(pinBrakeA,OUTPUT);
// pinMode(pinDirB,OUTPUT);
// pinMode(pinVelB,OUTPUT);
// pinMode(pinBrakeB,OUTPUT);
 myservo.attach(pinServo);
 digitalWrite(pinDirA,HIGH);
// digitalWrite(pinDirB,HIGH);
// analogWrite(pinVelB,110);
 //digitalWrite(pinBrakeB,HIGH);
 scambio(gradiGiu);
 scambio(gradiSu);
 scambio(gradiGiu);
 autoMode=true;
 Serial.println("1");
 for(int i=0;i<5;i++)
  {
  delay(1000);
  Serial.println("aspetta");
  }
 metroMovimento=true;
 Serial.println("lasciata fermata");
 analogWrite(pinVelA,vel);
 comando='z';
 controlloFermate=true;
 }

void loop()
 {
 delay(25);
 tempo++;
 if (autoMode)
   {
   automatismo();
   }
  else
   {
   if (!controlloFermate)
    {
    controlloRemoto();
    if(tempo>tempoFermato+10.5)
     {
     controlloFermate=true;
     }
    }
    else
     {
     controlloFermata();
     controlloRemoto(); 
     } 
   }
 if(Serial.available()>0)
  {
  comando=Serial.read();
  Serial.flush();
  }
 if(comando == 'a')
   {
   Serial.println("controllo Remoto attivo");
   autoMode=false;
   } 
  }

void controlloRemoto()
 {
 if(Serial.available()>0)
   {
   comando=Serial.read(); 
   Serial.flush();
   if(comando=='b')
    {
    Serial.println("autopilota attivo");
    controlloFermate=true;
    autoMode=true;  
    }
   if(comando=='c')
    {
    Serial.println("Il treno ha cambiato direzione (su)");
    digitalWrite(pinDirA,HIGH);  
    } 
   if(comando=='d')
    {
    Serial.println("Il treno ha cambiato direzione (giu)");
    digitalWrite(pinDirA,LOW);  
    }
   if(comando=='e')
    {
    if(metroMovimento)
     {
     digitalWrite(pinBrakeA,HIGH);
     metroMovimento=false;
     Serial.println("hai fermato il treno");
     }
    else
     {
     digitalWrite(pinBrakeA,LOW);  
     metroMovimento=true;
     Serial.println("hai fatto partire il treno");
     }
    } 
    if(comando=='f')
     {
     Serial.println("lo scambio si muove");
     scambio(gradiGiu);
     }
    if(comando=='g')
     {
     Serial.println("lo scambio si muove");
     scambio(gradiSu);  
     }
     if(comando=='h')
      {
      vel=dimmiNumero();
      Serial.print("la velocita del treno e stata cambiata a ");
      Serial.println(vel);
      analogWrite(pinVelA,vel);
      }
    }
   comando='z';
   } 

void automatismo() 
 {
 digitalWrite(pinBrakeA,LOW);

 if(contaFermate==1)
   hall=analogRead(5);
  else
 hall=analogRead(contaFermate); //leggo il sensore della prossima fermata
 //Serial.println(hall);
 if(hall>515 || hall<480)
  {
  digitalWrite(pinBrakeA,HIGH);
  metroMovimento=false;
  //Serial.print("treno arrivato alla ferrmata n* ");
  Serial.println(contaFermate); //-------------------------------
  if(contaFermate==1)
   {
   verso=true;
   Serial.println("verso true");
   digitalWrite(pinDirA,HIGH);
   }
  if(contaFermate==2 && verso)
   {
   scambio(gradiSu);
   }
  if(contaFermate==3)
   {
   digitalWrite(pinDirA,LOW); 
   if(verso)
     {
	 scambio(gradiGiu);
	 }
    else
	 {
	 scambio(gradiSu);
	 }
   }
  if(contaFermate==4)
   {
   verso=false;
   Serial.println("verso false");
   digitalWrite(pinDirA,HIGH); 
   }
  if(verso)
    {
    contaFermate++;
    //Serial.print("incremento ");
    //Serial.println(contaFermate);
    }
   else
    {
    contaFermate--;
    //Serial.print("decremento ");
    //Serial.println(contaFermate);
    }
  delay(5000);
  digitalWrite(pinBrakeA,LOW);
  metroMovimento=true;
  Serial.println("lasciata fermata"); 
  }
  //else
 // Serial.print("niente");
 }

/*void cambioVel(int ms,int valore, boolean incrementa) // valore= accelerazione iniziale o da raggiungere
                                                      // ms=periodo di accelerazione o decelerazione
 {
 for(int i=0;i<ms;i++)
 {
  if(incrementa && vel<254)
    {
	vel+=valore;
	vel=255;
	}
   else if ((!incrementa) && vel<254)
    {
    vel-=valore;
	vel=0;
	}
  digitalWrite(pinVel,vel);
  delay(1);  
  }
 if(!incrementa)
  {
  digitalWrite(
  }
 }*/

void scambio(int gradi) //12V
  {
  myservo.write(gradi);
  delay(2000);
  }

int dimmiNumero()
 {
 int numero=0;
 int cifra=-1;
 int numeroCifre=-1;
 int cifraConPosto=1;
 int posizioneDecimale=1;
 //Serial.println("dimmi quante cifre ha il numero");
 for(;numeroCifre<1;)
  {
  numeroCifre=Serial.read() - 48;
  }
// Serial.print("numeroCifre= ");
// Serial.println(numeroCifre);
// Serial.println("dimmi le cifre a partire dalla destra"); 
 for(int i=numeroCifre;i>0;i--)
  {
  for(;cifra<1;)
   {
   cifra=Serial.read() - 48;
   }
//   Serial.print("cifra presa= "); 
//   Serial.println(cifra);
   
   posizioneDecimale= potenza(10,i)/10;
//   Serial.print(" * "); 
//   Serial.println(posizioneDecimale);
   cifraConPosto=(cifra*posizioneDecimale);    
//   Serial.print("cifra registrata= "); 
//   Serial.println(cifraConPosto);
   cifra=(-1);
   numero= numero + cifraConPosto; 
   } 
 return numero;
 }
 
int potenza(int base,int esponente)
  {
  int risultato=base;
  for(int i=0;i<esponente-1;i++)
   {
   risultato=risultato * base;
   }
  return risultato;  
  }

void controlloFermata()
 {
 //delay(1000);
 int rilevamento=0;
 if(controlloFermate)
    {
    for(int i=1;i<5;i++)
     {
     if(i==1)
       rilevamento=analogRead(5);
      else
        rilevamento=analogRead(i);
     //Serial.println("controllo");
     if(rilevamento>507 || rilevamento<480)
//     hall>507 || hall<480
      {
      //tempo=millis();
      contaFermate=i;
      Serial.println(contaFermate);
      if(verso)
       {
       contaFermate++;
       }
      else
       {
       contaFermate--;
       }
      controlloFermate=false;
      }
     }
    }
//   else
//    {
//    contaTempo=millis();
//    //Serial.println(contaTempo);
//    if(contaTempo>(tempo+5))
//     {
//     controlloFermate=true;
//     }	
//    }
 }
