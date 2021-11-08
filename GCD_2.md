# Distpatch Group이란?

디스패치 그룹은 언제 사용할까?

일반적으로는 그룹으로 묶인 Task 들의 최종 종료 시점을 알고 싶을 때 사용합니다



아래 예시를 보면서 한번 살펴보겠습니다.

```
let group = DispatchGroup()

func a() {
    print("1")
}

func b() {
    sleep(1)
    print("2")
}

print("asd")

DispatchQueue.global().async(group: group) {
    a()
}

DispatchQueue.global().async(group: group) {
    b()
}

group.notify(queue: .main) {
    print("작업 끝!")
}
```



위와 같은 동기적인 작업을 수행한다고 했을 때, 

"1"

(1초 쉬고)

"2"

"작업 끝!"



이 순서대로 작업이 진행될 것 입니다. 

즉, 같은 그룹으로 묶여있는 모든 작업이 끝났을때, group.notify 가 실행되며 이 후 작업을 수행할 수 있게 됩니다.



하지만 일반적으로 여러 작업의 끝나는 끝 지점을 알고 싶을 때, 그 작업들이 비동기적으로 동작하는 경우가 많습니다.

만약 함수 b 클로져 내에서 비동기 함수를 호출하면 어떻게 될까요?

```swift
func b() {
    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
        print("2")
    }
}
```

만약 b함수가 위와 같다면, **"작업 끝!"** 이 프린트 된 후에 **"2"** 가 프린트 될 것 입니다.



이렇게 되면, 우리가 알고 싶었던 group내에 모든 작업이 끝나는 시점을 정확하게 알 수가 없어집니다.



이런 문제를 해결하기 위해, DispatchGroup은 enter와 leave 메소드를 제공하고 있습니다.

즉, ARC 가 retain count를 관리하듯 우리의 DispatchGroup 내의 task 상태를 직접 관리해주며, group내 모든 작업이 끝나는 시점을 지정해 줄 수 있습니다.



아래 예시를 살펴보겠습니다.

```swift
class ViewController: UIViewController {
    
    let labelTitle = UILabel()
    let labelDescription = UILabel()
    let imageView = UIImageView()
    let button: UIButton = {
        let btn = UIButton(frame: CGRect(x: 0, y: 0, width: 100, height: 50))
        btn.setTitle("시작", for: .normal)
        btn.addTarget(self, action: #selector(startAnimation), for: .touchUpInside)
        btn.translatesAutoresizingMaskIntoConstraints = false
        return btn
    }()
    
    var labelTitleCenterXConstraint: NSLayoutConstraint?
    var imageViewWidthConstraint: NSLayoutConstraint?
    var imageViewHeightConstraint: NSLayoutConstraint?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setUI()
    }
    
    func setUI() {
        
        view.addSubview(labelTitle)
        view.addSubview(labelDescription)
        view.addSubview(imageView)
        view.addSubview(button)
        
        labelTitle.translatesAutoresizingMaskIntoConstraints = false
        labelDescription.translatesAutoresizingMaskIntoConstraints = false
        imageView.translatesAutoresizingMaskIntoConstraints = false
        
        imageView.image = UIImage(systemName: "heart")
        labelTitle.textAlignment = .center
        labelDescription.textAlignment = .center
        
        setContraintsForAnimation()
        
        NSLayoutConstraint.activate([
    
            labelTitle.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            labelTitle.widthAnchor.constraint(equalToConstant: 200),
            labelTitle.heightAnchor.constraint(equalToConstant: 44),
            
            imageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            imageView.topAnchor.constraint(equalTo: labelTitle.bottomAnchor, constant: 10),
            
            labelDescription.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            labelDescription.topAnchor.constraint(equalTo: imageView.bottomAnchor, constant: 10),
            labelDescription.widthAnchor.constraint(equalToConstant: 200),
            labelDescription.heightAnchor.constraint(equalToConstant: 44),
            
            button.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            button.topAnchor.constraint(equalTo: labelDescription.bottomAnchor, constant: 10),
        ])
    }
    
    func setContraintsForAnimation() {
        
        labelTitle.text = "이미지가 나옵니다."
        labelDescription.text = "애니메이션 시작"
        
        labelTitleCenterXConstraint = labelTitle.centerXAnchor.constraint(equalTo: view.centerXAnchor, constant: 500)
        imageViewWidthConstraint = imageView.widthAnchor.constraint(equalToConstant: 0)
        imageViewHeightConstraint = imageView.heightAnchor.constraint(equalToConstant: 0)
        labelTitleCenterXConstraint?.isActive = true
        imageViewWidthConstraint?.isActive = true
        imageViewHeightConstraint?.isActive = true
        
        self.view.layoutIfNeeded()
    }
    
    @objc func startAnimation() {
        labelTitle.text = "이미지가 나옵니다."
        labelDescription.text = "애니메이션 시작"
        
        let group = DispatchGroup()
        
        group.enter()
        UIView.animate(withDuration: 1, animations: { [weak self] in
            guard let self = self else { return }
            self.labelTitleCenterXConstraint?.constant = 0
            self.view.layoutIfNeeded()
        }) { (_) in
            self.labelDescription.text = "아직 이미지는 애니메이션중"
            group.leave()
        }
        
        group.enter()
        UIView.animate(withDuration: 3, animations: { [weak self] in
            guard let self = self else { return }
            self.imageViewHeightConstraint?.constant = 200
            self.imageViewWidthConstraint?.constant =  200
            self.view.layoutIfNeeded()
        }) { (_) in
            group.leave()
        }
        
        group.notify(queue: DispatchQueue.main) { [weak self] in
            guard let self = self else { return }
            self.labelDescription.text = "애니메이션 끝"
            self.labelTitle.text = ""
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                self.labelTitleCenterXConstraint?.constant = 500
                self.imageViewHeightConstraint?.constant = 0
                self.imageViewWidthConstraint?.constant = 0
                self.view.layoutIfNeeded()
            }
        }
    }
}

```

다른 부분은 구현을 위한 코드이니, **startAnimation()** 함수만 보시면 이해가 빠르실 것 같습니다.



이 함수 내 코드들은 UIView의 animate 메소드를 통해, label과 imageView의 위치 및 크기를 변하게 하고 있습니다.

각 애니메이션이 돌아가고 끝날 때, group.enter() 와 group.leave()를 통해 이를 group에게 알려주고 있고, 

이에 따라, 모든 애니메이션이 끝나게 되면, "애니메이션 끝"이라는 텍스트를 노출합니다. 