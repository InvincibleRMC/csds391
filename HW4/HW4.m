clear; close;


d1 = zeros(1,100);
d2 = [zeros(1,75),ones(1,25)];
d3 = [zeros(1,50),ones(1,50)];
d4 = [zeros(1,25),ones(1,75)];
d5 = ones(1,100);

d2 = d2(randperm(length(d2)));
d3 = d3(randperm(length(d3)));
d4 = d4(randperm(length(d4)));
data = [d1;d2;d3;d4;d5];
size=100;
plotFromData(data,size);

function plotFromData(Data,size)

    tiledlayout(5,2);
    for j= 1:5
        hi_initial = [0.1, 0.2, 0.4, 0.2, 0.1]; % initial probability of having any bag
        hi = hi_initial; % stick that into our current bag prob distribution
        pLime = [0, 0.25, 0.5, 0.75, 1]; % probability of drawing a lime candy from each bag
        pCherry = [1, 0.75, 0.5, 0.25, 0]; % probability of drawing a lime candy from each bag
        
    
        numTests = size; % number of tests that we're doing
        hi_history = hi; % used for graphing
        chanceOfLime = sum(hi .* pLime); % history of the chance of something being lime
        
        
        i = 1;
        while numTests > i
            % since we know we have the lime-only bag, we can assume all candies
            % drawn are lime
            if Data(j,i) == 1
                prob = pLime;
            else
                  prob = pCherry; 
            end
           
            hi = hi .* prob; % P(h_i | d) = P(hi) * P(d | hi)
            hi = hi / sum(hi); % rescale so chance of everything is 1 (alpha)
            hi_history = [hi_history; hi];
            chanceOfLime = [chanceOfLime; sum(hi .* pLime)]; % P(X | d)
            i=i+1;
        end
        
        
       ax1 = nexttile;
        plot(hi_history, '--s');
        xlabel("Number of observations in d")
        ylabel(["P(hi | d)bag=", j],"FontSize",6)
      ax2 = nexttile;
        plot(chanceOfLime, '--s');
        xlabel("Number of observations in d")
        ylabel(["P(DN-+1=lime | d1:dN)bag=",j],"FontSize",6)
        disp(hi_history);
         
    end
   
end
