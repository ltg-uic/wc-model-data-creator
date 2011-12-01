clear
disp("Running population model for WallCology...");

// Define the model
function xdot=simpleModel(t,x,r1,r2,k1,k2,al11,al12,al21,al22,a11,a12,a21,a22,d1,d2,c1,c2,a11,a21,a12,a22)
    xdot=[
    x(1)*(r1*(1-al11*x(1)/k1-al12*x(2)/k1)-a11*x(3)-a12*x(4));
    x(2)*(r2*(1-al21*x(1)/k2-al22*x(2)/k2)-a21*x(3)-a22*x(4));
    x(3)*(c1*a11*x(1)+c1*a21*x(2)-d1);
    x(4)*(c2*a12*x(1)+c2*a22*x(2)-d2)
    ]
endfunction

// Set the initial conditions and the lenght of the evaluation interval
//=[40;35;4;3]
x0=[40;35;4;3]
tf=600;

t0=0;
dp = 100;
step = tf/(dp-1);
t = t0:step:tf;

// Assign values to all the 17 parameters of the model
r1=.1;
r2=.1;

k1=85;
k2=95;

al11=1;
al12=.3;
al21=.4;
al22=1;

a11=.01;
a12=.004;
a21=.005;
a22=.012;

d1=.4;
d2=.4;

c1=1;
c2=1;

//(don't touch them!!!!)
//They only impact the initial values that are already
//"written-in-stone" and used to retrieve data form the DB!
ci1=1.8;  // 1.8 coefficient of stable system 
ci2=1.8;  // 1.8
ci3=5;    // 5
ci4=5;    // 5
ci5=3.35; // 3.35
//(don't touch them!!!!)

// These are the coefficients that define the carrying capacity
// and final plateau states. Use these!!!!
cf1=2.3;  // 1.8
cf2=.65;  // 1.8
cf3=4.8;    // 5
cf4=2.0;    // 5
cf5=2.0;  // 3.35


// Solve the system
lst=list(simpleModel,r1,r2,k1,k2,al11,al12,al21,al22,a11,a12,a21,a22,d1,d2,c1,c2,a11,a21,a12,a22); 
sol=ode(x0,t0,t,lst);

// Add predator data
[nr,nc]=size(sol);
sol(5,:)=zeros(1,nc);
sol(5,:)=sol(4,:);  // Uncomment this line to add predator dynamic

//Corrections
sol(1,1)=ci1*sol(1,1);
sol(2,1)=ci2*sol(2,1);
sol(3,1)=ci3*sol(3,1);
sol(4,1)=ci4*sol(4,1);
sol(5,1)=ci5*sol(5,1);
sol(1,2:$)=cf1*sol(1,2:$);
sol(2,2:$)=cf2*sol(2,2:$);
sol(3,2:$)=cf3*sol(3,2:$);
sol(4,2:$)=cf4*sol(4,2:$);
sol(5,2:$)=cf5*sol(5,2:$);

// Plot the population curves
clf();
plot2d(t, sol', [2,5,2,5,3]);
// Change thickness
a=gca();
line= a.children(1).children(1);
line.thickness = 3; 
line= a.children(1).children(2);
line.thickness = 3; 
line= a.children(1).children(3);
line.thickness = 3; 
//Change colors

// Add legend
hl=legend(['Scum';'Fuzz';'Scum eaters'; 'Fuzz eaters'; 'Predator']);

// Store results
chdir('/Users/tebemis/Workspace/wallcology-model-data-creator/src/main/resources/');
fid = mopen("wc_experiment_data.txt", "w");
if (fid == -1)
    error('cannot open file for writing');
end
for rowIndex = 1:nr+1
    for colIndex = 1:nc
        mfprintf(fid, "%d,", sol(rowIndex, colIndex));
    end
    mfprintf(fid, "\n");
end
mclose(fid);

disp("... done!");
clear
