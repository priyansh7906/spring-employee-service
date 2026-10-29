package com.example.TestingAppApplication;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
class TestingAppApplicationTests {


    @BeforeEach
    void setup(){
        log.info(" starting method setup ");
    }

    @AfterEach
    void TearDown(){
        log.info(" ending  method setup");
    }

    @BeforeAll
    static void setupOnce(){
        log.info(" starting the setupOnce method setup");
    }

    @AfterAll
    static void TearDownOnce(){
        log.info(" Ending the tearDownOnce  method setup");
    }

	@Test
//    @Disabled
	void contextLoads() {
        log.info("test one is running");
	}

    @Test
//    @DisplayName("displayTestTwo")
    void testNumberTwo(){
        int a = 6;
        int b = 2;
        int result=addTwoNumbers(a,b);

//        Assertions.assertEquals(3,result);
        Assertions.assertThat(result)
                .isEqualTo(8)
                .isCloseTo(9, Offset.offset(9));
    }

    int addTwoNumbers(int a,int b){
        log.info("test two is running");
        return a+b;
    }


    @Test
    void testDivideNumber_WhenDenominatorIsZero_ThenArithmeticResult(){
        int a = 10;
        int b = 2;
        double result=DivideTwoNumbers(a,b);
        System.out.println(result);

    }

    double DivideTwoNumbers(int a,int b){
       try {
           return a/b;
       }catch (ArithmeticException e){
           log.error("arithmetic exception occured" +e.getLocalizedMessage());
           throw new ArithmeticException(e.getLocalizedMessage());
       }
    }




}
